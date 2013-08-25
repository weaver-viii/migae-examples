(ns cpj1.theServlet
  (:import (javax.servlet.http HttpServlet
                               HttpServletRequest
                               HttpServletResponse)
           (javax.servlet ServletConfig))
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:require [cpj1.impl :as impl]
            [ring.util.servlet :as ring]))

;; ring.util.servlet contains what is essentially HttpServlet POJO<->Clojure
;; adapter code; it really should be called ring-httpservlet-adapter, by
;; analogy to ring-jetty-adapter.  All it really does is convert
;; rqst/resp POJOs to and from Clojure maps. (see ring-jetty-adapter,
;; ring-servlet)

;; Note that this -service function will be compiled to a distinct
;; Java object (theServlet$_service.class); in other words it will not
;; be compiled into the object generated by gen-class as an extension
;; of HttpServlet (theServlet.class).
(defn -service
  ^{:doc "Servlet service method.  Call ring servlet adapter code to
  convert incoming HttpServletRequest POJO to a Clojure map, then call
  our handler implementation that generates the output map, then call
  another ring adapter to convert that Clojure map to the outgoing
  HttpServletResponse POJO."}
  [^HttpServlet this
   ^HttpServletRequest rqst
   ^HttpServletRequest resp]
  (do
    (println "cpj1.theServlet -service method implementation invoked")
    (let [request-map  (ring/build-request-map rqst)
          response-map (impl/theRouter request-map)]
      (when response-map
        (ring/update-servlet-response resp response-map)))))

;; We can just as easily implement -doGet etc. instead of -service,
;; since they have the same arg structure.  Try uncommenting the
;; -doGet code below and commenting out -service (above); make the
;; corresponding changes to impl.clj.  Then recompile and test.

;; (defn -doGet
;;   [^HttpServlet this
;;    ^HttpServletRequest rqst
;;    ^HttpServletRequest resp]
;;   (do
;;     (println "cpj1.theServlet -doGet method implementation invoked")
;;     (let [request-map  (ring/build-request-map rqst)
;;           response-map (impl/doGet request-map)]
;;       (when response-map
;;         (ring/update-servlet-response resp response-map)))))


