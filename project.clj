(defproject real-world-clojure-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]
                 [aero/aero "1.1.6"]
                 [io.pedestal/pedestal.service "0.8.2-beta-6"]
                 [io.pedestal/pedestal.route "0.8.2-beta-6"]
                 [io.pedestal/pedestal.jetty "0.8.2-beta-6"]
                 [org.slf4j/slf4j-simple "2.0.16"]
                 [com.stuartsierra/component "1.2.0"]
                 [com.stuartsierra/component.repl "1.0.0"]
                 [clj-http "3.13.1"]
                 [cheshire "6.2.0"]
                 [prismatic/schema "1.4.1"]]
  :plugins [[lein-eftest "0.6.0"]]
  :eftest {:multithread? false}
  :main ^:skip-aot real-world-clojure-api.core
  :target-path "target/%s"
  :paths ["src" "resources" "dev"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
