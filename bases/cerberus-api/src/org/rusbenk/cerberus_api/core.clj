(ns org.rusbenk.cerberus-api.core
  (:gen-class)
  (:require [reitit.ring :as r.ring]
            [ring.adapter.jetty :as ring.jetty]
            [muuntaja.core :as muuntaja]
            [reitit.ring.coercion :as r.ring.coercion]
            [reitit.coercion.malli :as r.coercion.malli]
            [reitit.swagger :as r.swagger]
            [reitit.swagger-ui :as r.swagger-ui]
            [reitit.ring.middleware.muuntaja :as r.ring.muuntaja]
            [reitit.ring.middleware.parameters :as r.ring.parameters])
  (:import (org.eclipse.jetty.server Server)))

(defonce ^:private server (atom nil))

(defn gen-routes []
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info        "Cerberus API"
                     :description "Identity & Access Management"}
           :handler (r.swagger/create-swagger-handler)}}]
   ["/doc/*"
    {:get {:no-doc  :true
           :handler (r.swagger-ui/create-swagger-ui-handler
                      {:config {:validatorUrl     nil
                                :operationsSorter "alpha"}})}}]
   ["/api"
    ["/health" {:get {:responses {200 {:body {:state :keyword}}}
                      :handler   (constantly
                                   {:status 200
                                    :body   {:state :healthy}})}}]]])

(defn gen-router [routes]
  (r.ring/router
    routes
    {:data {
            ;; Data coercion with Malli
            :coercion   (r.coercion.malli/create)

            ;; Content Negotiation
            :muuntaja   muuntaja/instance

            ;; Middlewares
            :middleware [r.swagger/swagger-feature
                         r.ring.parameters/parameters-middleware

                         r.ring.muuntaja/format-negotiate-middleware
                         r.ring.muuntaja/format-response-middleware

                         ;; TODO: Add exception handler

                         r.ring.muuntaja/format-request-middleware

                         r.ring.coercion/coerce-response-middleware
                         r.ring.coercion/coerce-request-middleware]}}))

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


(comment

  (do

    (stop!)

    (start! {:join? false
             :port  3000
             :host  "0.0.0.0"})

    )

  )