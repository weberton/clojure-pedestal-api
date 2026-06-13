(ns real-world-clojure-api.components.pedestal-component
  (:require [cheshire.core :as json]
            [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [io.pedestal.http.content-negotiation :as content-negotiation]
            [io.pedestal.http.route :as route]
            [io.pedestal.interceptor :as interceptor]))


(defn response
  ([status]
   (response status nil))
  ([status body]
   (merge
     {:status  status
      :headers {"Content-Type" "application/json"}}
     (when body {:body body}))))

(def ok (partial response 200))

(def not-found (partial response 404))

(defn respond-hello [request]
  {:status 200 :body "Hello, world!"})


(defn get-todo-by-id
  [{:keys [in-memory-state-component]} todo-id]
  (->> @(:state-atom in-memory-state-component)
       (filter (fn [todo]
                 (= todo-id (:id todo))))
       (first)))

(def get-todo-handler
  {:name :echo
   :enter
   (fn [{:keys [dependencies] :as context}]
     (println "get-todo-handler" (keys context))
     (let [request (:request context)
           todo (get-todo-by-id dependencies (-> request :path-params :todo-id))
           response (if todo (ok (json/encode todo))
                             (not-found))]
       (assoc context :response response)))})

(comment
  [{:id    (random-uuid)
    :name  "My todo list"
    :items [{:id     (random-uuid)
             :name   "Make my new youtube video"
             :status :created}
            ]}
   {:id    (random-uuid)
    :name  "Empty todo list"
    :items []}])

(def routes
  (route/expand-routes
    #{["/greet" :get respond-hello :route-name :greet]
      ["/todo/:todo-id" :get get-todo-handler :route-name :get-todo]}))

(def url-for (route/url-for-routes routes))

(defn inject-dependencies
  [dependencies]
  (interceptor/interceptor
    {:name  ::inject-dependencies
     :enter (fn [context]
              (assoc context :dependencies dependencies))}))

(def supported-types ["application/json"])

(def content-negotiation-interceptor (content-negotiation/negotiate-content supported-types))

(defn create-server
  [config]
  (http/create-server
    {::http/routes routes
     ::http/type   :jetty
     ::http/join?  false
     ::http/port   (-> config
                       :server
                       :port)}))

(defn start
  [config]
  (http/start (create-server config)))

(defrecord PedestalComponent
  [config example-component in-memory-state-component]
  component/Lifecycle

  (start [component]
    (println ";; Starting PedestalComponent...!")
    (let [server (-> {::http/routes routes
                      ::http/type   :jetty
                      ::http/join?  false
                      ::http/port   (-> config
                                        :server
                                        :port)}
                     (http/default-interceptors)
                     (update ::http/interceptors concat [
                                                         (inject-dependencies component)
                                                         content-negotiation-interceptor])
                     (http/create-server)
                     (http/start))]
      (assoc component :server server)))

  (stop [component]
    (println ";; Stopping PedestalComponent....!")
    (when-let [server (:server component)]
      (http/stop server))
    (assoc component :server nil)))


(defn new-pedestal-component [config]
  (map->PedestalComponent {:config config}))