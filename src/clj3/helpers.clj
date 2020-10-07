(ns clj3.helpers
  (:require [clostache.parser :refer [render-resource]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [clojure.java.io :as io]
            [clj-http.client :as client]))

(def defaults
  {:app "Work thingy"
   :title "Work thingy"
   :anti-forgery-field (fn [_] (anti-forgery-field))})

(def default-partials
  [:header :footer])

(defn render-page
  "Pass in the template name (a string, sans its .mustache
filename extension), the data for the template (a map), and a list of
partials (keywords) corresponding to like-named template filenames."
  ([defaults default-partials template]
    (render-page defaults default-partials template {}))
  ([defaults default-partials template data]
    (render-page defaults default-partials template data {}))
  ([defaults default-partials template data partials]
  (render-resource
    (str "templates/" template ".mustache")
    (merge defaults data)
    (reduce (fn [accum pt] ;; "pt" is the name (as a keyword) of the partial.
              (assoc accum pt (slurp (io/resource (str "templates/"
                                                       (name pt)
                                                       ".mustache")))))
            {}
            (concat default-partials partials)))))

(def render-page-with-defaults
  (partial render-page
           defaults default-partials))

(defn api-request
  [api-uri]
  (client/get api-uri {:as :auto}))
