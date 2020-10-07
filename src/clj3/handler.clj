(ns clj3.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [clj3.helpers :refer [render-page-with-defaults api-request]]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.pprint :refer [pprint]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] 
       (render-page-with-defaults "home"
                                  {:title "Home"}
                                  [:api_request_form :api_request_result]))
  (POST "/api-request" [api-uri]
        (render-page-with-defaults "home"
                                   {:title "Result"
                                    :result true
                                    :api-uri api-uri
                                    :response (try
                                                (-> api-uri api-request pprint with-out-str)
                                                (catch Exception e (str "Error: " (.getMessage e))))}
                                   [:api_request_form :api_request_result]))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults (wrap-params app-routes) site-defaults))
