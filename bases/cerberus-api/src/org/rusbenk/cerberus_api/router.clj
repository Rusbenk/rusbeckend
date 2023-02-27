(ns org.rusbenk.cerberus-api.router
  (:require [muuntaja.core :as muuntaja]
            [reitit.coercion.malli :as r.coercion.malli]
            [reitit.ring :as r.ring]
            [reitit.ring.coercion :as r.ring.coercion]
            [reitit.ring.middleware.muuntaja :as r.ring.muuntaja]
            [reitit.ring.middleware.parameters :as r.ring.parameters]
            [reitit.swagger :as r.swagger]
            [reitit.swagger-ui :as r.swagger-ui]))

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