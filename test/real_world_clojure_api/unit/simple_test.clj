(ns real-world-clojure-api.unit.simple_test
  (:require [clojure.test :refer :all]
            [real-world-clojure-api.components.pedestal-component :refer [url-for]]))

(deftest a-simple-passint-test
  (is (= 1 1)))


(deftest url-for-test
  (testing "greet endpoint url"
    (is (= "/greet" (url-for :greet))))

  (testing "get todo by id endpoint url"
    (let [todo-id (random-uuid)]
      (is (= (str "/todo/" todo-id) (url-for :get-todo {:path-params {:todo-id todo-id}}))))))