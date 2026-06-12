(ns real-world-clojure-api.core
  (:gen-class)
  (:require [real-world-clojure-api.config :as config]
            [real-world-clojure-api.components.example-component :as example-component]
            [com.stuartsierra.component :as component]
            [real-world-clojure-api.components.pedestal-component
             :as pedestal-component]
            [real-world-clojure-api.components.in_memory_state_component :as in-memory-state-component]))



(defn real-world-clojure-api-system
  [config]
  (component/system-map :example-component (example-component/new-example-component config)
                        :in-memory-state-component (in-memory-state-component/new_in_memory_state_component config)
                        :pedestal-component (component/using
                                              (pedestal-component/new-pedestal-component config)
                                              [:example-component
                                               :in-memory-state-component])))

(defn -main
  [& args]
  (let [system (-> (config/read-config )
                   (real-world-clojure-api-system)
                   (com.stuartsierra.component/start-system))]
  (println "Starting Real-World Clojure API Server with config" system)
  (.addShutdownHook (Runtime/getRuntime)
                    (new Thread #(com.stuartsierra.component/stop-system system)))))
