(ns real-world-clojure-api.component.api-test
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [real-world-clojure-api.core :as core]
            [real-world-clojure-api.components.pedestal-component :refer [url-for]])
  (:import (java.net ServerSocket)))


(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))


(defn sut->url
  [sut  path]
  (str/join ["http://localhost:"
             (-> sut
                 :pedestal-component :config :server :port)
             path]))

(defn get-free-port
  []
  (with-open [socket (ServerSocket. 0)]
    (.getLocalPort socket)))

(deftest greeting-test
  ;sut - system under test
  (with-system [sut (core/real-world-clojure-api-system {:server {:port (get-free-port)}})]
               (is (= {:body   "Hello, world!"
                       :status 200}
                      (-> (sut->url sut (url-for  :greet))
                          (client/get)
                          (select-keys [:body :status]))))

               (is (= 1 1))))

(deftest get-todo-test
  (let [todo-id-1 (random-uuid)
        todo-1 {:id    todo-id-1
                :name  "My todo for test"
                :items [{
                         :id   (random-uuid)
                         :name "finish the test"
                         }]}]
    (with-system [sut (core/real-world-clojure-api-system {:server {:port (get-free-port)}})]
                 (reset! (-> sut :in-memory-state-component :state-atom) [todo-1])
                 (is (= {:body   (pr-str todo-1)
                         :status 200}
                        (-> (sut->url sut (url-for :get-todo {:path-params {:todo-id todo-id-1}}))
                            (client/get)
                            (select-keys [:body :status]))))
                 (testing "Empty body is returned form random id"
                   (is (= {:body   ""
                           :status 200}
                          (-> (sut->url sut (url-for :get-todo {:path-params {:todo-id (random-uuid)}}))
                              (client/get)
                              (select-keys [:body :status])))))

                 (is (= 1 1)))))

(deftest a-simple-api-test
  (is (= 0 0)))
