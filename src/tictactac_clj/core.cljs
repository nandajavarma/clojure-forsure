(ns tictactac-clj.core
    (:require  [reagent.core :as reagent :refer [atom]]
               [reagent.dom :as rd]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload
(def board-dimension 3)

(defn game-board
  [dimension]
  (vec (repeat dimension (vec (repeat dimension :empty))))
  )


(defonce app-state (reagent/atom  {:page
                            {:title "Tic Tac Toe"
                             :message "Play a game of Tic Tac Toe"}
                            :board (game-board board-dimension)}))

(defn cell-empty
  [x-cell y-cell]
  ^{:key (str x-cell y-cell)}
  [:rect {:width 0.9
          :height 0.9
          :fill "grey"
          :x x-cell
          :y y-cell
          :on-click
          (fn rectangle-click [e]
            (println (str "Cell " x-cell y-cell " was clicked cell-empty"))
            (println
             (swap! app-state assoc-in [:board x-cell y-cell] :cross))
  )}])

(defn cell-cross
  [x-cell y-cell]
  ^{:key (str x-cell y-cell)}
  [:g {:stroke "purple"
       :stroke-width 0.4
       :stroke-linecap "round"
       :transform
       (str "translate(" (+ 0.42 x-cell) "," (+ 0.42 y-cell) ") "
        "scale(0.3)")}
       [:line {:x1 -1 :y1 -1 :x2 1 :y2 1}]
       [:line {:x1 1 :y1 -1 :x2 -1 :y2 1}]])

(defn cell-circle
  [x-cell y-cell]
  ^{:key (str x-cell y-cell)}
  [:circle {:r 0.36
            :fill "white"
            :stroke "green"
            :stroke-width 0.1
            :cx (+ 0.42 x-cell)
            :cy (+ 0.42 y-cell)}])

(defn tictactac-game []
  [:div
   [:h1 (get-in @app-state [:page :title])]
   [:p (get-in @app-state [:page :message])]
   [:button {:on-click (fn new-game-click [e] (swap! app-state assoc :board (game-board board-dimension)))} "Start a new game"]

   [:center
    [:svg {:view-box "0 0 3 3"
           :width 500
           :height 500}
     (for [x-cell (range (count (:board @app-state)))
           y-cell (range (count (:board @app-state)))]
       ^{:key (str x-cell y-cell)}
       (case (get-in @app-state [:board x-cell y-cell])
         :empty (cell-empty x-cell y-cell)
         :cross (cell-cross x-cell y-cell)
         :circle (cell-circle x-cell y-cell)))
     ]]])


(rd/render [tictactac-game]
                         (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  (swap! app-state update-in [:__figwheel_counter] inc)
)

