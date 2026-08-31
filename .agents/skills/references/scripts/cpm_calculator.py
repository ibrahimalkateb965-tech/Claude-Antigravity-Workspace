# -*- coding: utf-8 -*-
"""
خوارزمية حساب المسار الحرج (Critical Path Method - CPM)
تأليف: وكيل schedule-builder
"""

import collections

class Activity:
    def __init__(self, activity_id, name, duration, predecessors=None):
        self.id = activity_id
        self.name = name
        self.duration = duration
        # Predecessors should be a list of activity IDs
        self.predecessors = predecessors if predecessors else []
        self.successors = []
        
        # CPM fields
        self.es = 0  # Early Start
        self.ef = 0  # Early Finish
        self.ls = 0  # Late Start
        self.lf = 0  # Late Finish
        self.tf = 0  # Total Float
        self.ff = 0  # Free Float
        self.is_critical = False

    def __repr__(self):
        return f"Activity({self.id}, {self.name}, Dur: {self.duration}, ES: {self.es}, EF: {self.ef}, LS: {self.ls}, LF: {self.lf}, TF: {self.tf}, Critical: {self.is_critical})"


class CPMCalculator:
    def __init__(self):
        self.activities = {}

    def add_activity(self, activity_id, name, duration, predecessors=None):
        activity = Activity(activity_id, name, duration, predecessors)
        self.activities[activity_id] = activity
        return activity

    def _build_relations(self):
        # Clear successors first
        for act in self.activities.values():
            act.successors = []
            
        # Build successors list
        for act in self.activities.values():
            for pred_id in act.predecessors:
                if pred_id in self.activities:
                    self.activities[pred_id].successors.append(act.id)

    def _topological_sort(self):
        self._build_relations()
        
        # Calculate in-degree for each node
        in_degree = {act_id: 0 for act_id in self.activities}
        for act in self.activities.values():
            for successor_id in act.successors:
                in_degree[successor_id] += 1
                
        # Queue for nodes with in-degree 0
        queue = collections.deque([act_id for act_id, deg in in_degree.items() if deg == 0])
        order = []
        
        while queue:
            node_id = queue.popleft()
            order.append(node_id)
            for succ_id in self.activities[node_id].successors:
                in_degree[succ_id] -= 1
                if in_degree[succ_id] == 0:
                    queue.append(succ_id)
                    
        if len(order) != len(self.activities):
            raise ValueError("خطأ: توجد حلقة مغلقة (Loop) في العلاقات بين الأنشطة!")
            
        return order

    def calculate(self):
        if not self.activities:
            return {}

        # 1. Sort topologically for Forward Pass
        order = self._topological_sort()

        # 2. Forward Pass (ES & EF)
        for act_id in order:
            act = self.activities[act_id]
            if not act.predecessors:
                act.es = 1  # Start on day 1
            else:
                # ES = max(EF of predecessors) + 1
                max_ef = 0
                for pred_id in act.predecessors:
                    if pred_id in self.activities:
                        max_ef = max(max_ef, self.activities[pred_id].ef)
                act.es = max_ef + 1
            
            act.ef = act.es + act.duration - 1

        # 3. Find project finish time (max EF of all nodes)
        project_finish = max(act.ef for act in self.activities.values())

        # 4. Backward Pass (LS & LF)
        # Process in reverse topological order
        for act_id in reversed(order):
            act = self.activities[act_id]
            if not act.successors:
                act.lf = project_finish
            else:
                # LF = min(LS of successors) - 1
                min_ls = float('inf')
                for succ_id in act.successors:
                    if succ_id in self.activities:
                        min_ls = min(min_ls, self.activities[succ_id].ls)
                act.lf = min_ls - 1
                
            act.ls = act.lf - act.duration + 1

        # 5. Calculate Floats & Critical Path
        for act in self.activities.values():
            act.tf = act.ls - act.es
            
            # Free Float = min(ES of successors) - EF of activity - 1
            if not act.successors:
                act.ff = project_finish - act.ef
            else:
                min_succ_es = float('inf')
                for succ_id in act.successors:
                    if succ_id in self.activities:
                        min_succ_es = min(min_succ_es, self.activities[succ_id].es)
                act.ff = max(0, min_succ_es - act.ef - 1)
                
            act.is_critical = (act.tf == 0)

        return self.activities

# تجربة سريعة للتحقق
if __name__ == "__main__":
    calc = CPMCalculator()
    calc.add_activity("A", "التجهيز والتحضير", 5)
    calc.add_activity("B", "الحفر والتسوية", 10, ["A"])
    calc.add_activity("C", "صب الخرسانة العادية", 3, ["B"])
    calc.add_activity("D", "أعمال المباني", 7, ["B"])
    calc.add_activity("E", "اللياسة والدهان", 10, ["C", "D"])
    
    results = calc.calculate()
    print("نتائج حساب المسار الحرج:")
    for act_id, act in results.items():
        print(f"نشاط {act_id}: {act.name} | البداية: {act.es} | النهاية: {act.ef} | الاحتياطي: {act.tf} | حرج: {act.is_critical}")
