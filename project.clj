(defproject io.testfile.core "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.2.4"]
                 [ch.qos.logback/logback-classic "1.4.4"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-defaults "0.3.3"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring-ratelimit "0.2.2"]
                 [compojure "1.6.2"]
                 [cheshire "5.10.2"]
                 [com.github.javafaker/javafaker "1.0.2"]]
  :main ^:skip-aot io.testfile.core
  :uberjar-name "io.testfile-standalone.jar"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
