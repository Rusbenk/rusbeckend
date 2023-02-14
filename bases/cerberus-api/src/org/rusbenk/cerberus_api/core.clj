(ns org.rusbenk.cerberus-api.core
  (:gen-class)
  (:require [reitit.ring :as r.ring]
            [ring.adapter.jetty :as ring.jetty]
            [reitit.ring.middleware.parameters :as r.ring.parameters])
  (:import (org.eclipse.jetty.server Server)))

(defonce ^:private server (atom nil))

(defn gen-routes []
  [["/api"
    ["/health" {:get {:handler (constantly
                                 {:status  200
                                  :headers {"Content-Type" "application/edn"}
                                  :body    "{:state :healthy}"})}}]]])

(defn gen-router [routes]
  (r.ring/router
    routes
    {:data {
            ;; Data coercion with Malli
            ;:coercion reitit.coercion.spec/coercion

            ;; Content Negotiation
            ;:muuntaja m/instance

            ;; Middlewares
            :middleware [r.ring.parameters/parameters-middleware]}}))

(defn gen-app []
  (r.ring/ring-handler
    (gen-router (gen-routes))))

(defn start! [opts]
  (when-not (nil? @server)
    (throw (ex-info "Server was already started" nil)))
  (reset! server
          (ring.jetty/run-jetty
            (gen-app)
            opts)))

(defn stop! []
  (when (nil? @server)
    (throw (ex-info "Server is already stopped" nil)))
  (let [^Server s @server]
    (.stop s)
    (reset! server nil)))

;; Namespace de rotas
;; Namespace de Handlers
;; Namespace de config
;; - Setup: Content Negotiation (req+res JSON)
;; - Setup: Coercion + Validation (malli)
;; - Setup: Swagger

(defn -main [& _]
  (println "TODO"))


(comment


  (do

    (stop!)

    (start! {:join? false
             :port  3000
             :host  "0.0.0.0"})

    )

  )