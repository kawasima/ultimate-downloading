(ns ultimate-downloading.endpoint.example
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [resource-response content-type header]]
            [garden.core :refer [css]]
            [garden.units :refer [px em]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css include-js]]
            [clojure.java.io :as io])
  (:import [java.util UUID]))

(defn index []
  (html5
   [:head
    [:meta {:charset "utf8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
    (include-css "/assets/Semantic-UI/semantic.css")
    (include-js "/assets/jquery/dist/jquery.min.js")]
   [:body
    [:div.ui.container
     [:div.ui.segment
      [:button#btn-download.ui.primary.large.icon.button {:type "button"}
       "Download"]
      [:p.progress "あと" [:span "0"] "秒"]]]
    (include-js "/js/ultimate.js")]))

(defn example-endpoint [config]
  (context "/example" []
    (GET "/" []
      (index))
    (GET "/request-download" []
      {:body {:id (.toString (UUID/randomUUID)) :delay (inc (rand-int 20))}})
    (GET "/request-download/:id" [id]
      (if (< (rand-int 10) 5)
        {:body {:status "done" :url (str "/example/download/" id)}}
        {:body {:status "working" :id id :delay (inc (rand-int 5))}}))
    (GET "/download/:id" [id]
      (-> (resource-response "example.pdf")
          (content-type "application/force-download")
          (header "Content-disposition" (str "attachment; filename=\"" id ".pdf\""))))))
