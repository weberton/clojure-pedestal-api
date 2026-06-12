(ns real-world-clojure-api.components.in_memory_state_component
  (:require [com.stuartsierra.component :as component]))


(defrecord InMemoryStateComponent
  [config]
  component/Lifecycle

  (start [component]
    (println ";; Starting InMemoryStateComponent...!")
      (assoc component :state-atom (atom [])))

  (stop [component]
    (println ";; Stopping InMemoryStateComponent....!")
    (assoc component :state-atom nil)))


(defn new_in_memory_state_component [config]
  (map->InMemoryStateComponent {:config config}))