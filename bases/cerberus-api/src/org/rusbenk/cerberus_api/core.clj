(ns org.rusbenk.cerberus-api.core
  (:gen-class)
  (:require
    [org.rusbenk.cerberus-api.router :as router]
    [reitit.ring :as r.ring]
    [ring.adapter.jetty :as ring.jetty])
  (:import (org.eclipse.jetty.server Server)))

(defonce ^:private server (atom nil))

(defn gen-app []
  (r.ring/ring-handler
    (router/gen-router (router/gen-routes))))

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