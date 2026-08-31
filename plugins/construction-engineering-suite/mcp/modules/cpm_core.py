#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
خوارزمية حساب المسار الحرج (Critical Path Method - CPM Core)
"""

import datetime
from typing import List, Dict, Any, Optional

class Activity:
    def __init__(self, activity_id: str, name: str, duration: int, predecessors: Optional[List[str]] = None, wbs: str = ""):
        self.id = str(activity_id).strip()
        self.name = str(name).strip()
        self.duration = int(duration)
        self.predecessors = [str(p).strip() for p in predecessors] if predecessors else []
        self.successors: List[str] = []
        self.wbs = wbs
        
        # CPM calculations
        self.es = 0  # Early Start
        self.ef = 0  # Early Finish
        self.ls = 0  # Late Start
        self.lf = 0  # Late Finish
        self.tf = 0  # Total Float
        self.ff = 0  # Free Float
        self.is_critical = False

    def to_dict(self) -> Dict[str, Any]:
        return {
            "id": self.id,
            "wbs": self.wbs,
            "name": self.name,
            "duration": self.duration,
            "predecessors": self.predecessors,
            "es": self.es,
            "ef": self.ef,
            "ls": self.ls,
            "lf": self.lf,
            "tf": self.tf,
            "ff": self.ff,
            "is_critical": self.is_critical
        }


class CPMCalculator:
    def __init__(self):
        self.activities: Dict[str, Activity] = {}

    def add_activity(self, activity_id: str, name: str, duration: int, predecessors: Optional[List[str]] = None, wbs: str = ""):
        act = Activity(activity_id, name, duration, predecessors, wbs)
        self.activities[act.id] = act
        return act

    def _build_relations(self):
        for act in self.activities.values():
            act.successors = []
        for act in self.activities.values():
            for pred_id in act.predecessors:
                if pred_id in self.activities:
                    self.activities[pred_id].successors.append(act.id)

    def _topological_sort(self) -> List[str]:
        self._build_relations()
        in_degree = {act_id: 0 for act_id in self.activities}
        for act in self.activities.values():
            for succ_id in act.successors:
                in_degree[succ_id] += 1
                
        queue = [act_id for act_id, deg in in_degree.items() if deg == 0]
        order = []
        
        while queue:
            curr = queue.pop(0)
            order.append(curr)
            for succ_id in self.activities[curr].successors:
                in_degree[succ_id] -= 1
                if in_degree[succ_id] == 0:
                    queue.append(succ_id)
                    
        if len(order) != len(self.activities):
            # Fallback to keys if cycle detected
            return list(self.activities.keys())
        return order

    def calculate(self) -> Dict[str, Any]:
        order = self._topological_sort()
        
        # Forward Pass
        for act_id in order:
            act = self.activities[act_id]
            if not act.predecessors:
                act.es = 0
            else:
                max_ef = 0
                for pred_id in act.predecessors:
                    if pred_id in self.activities:
                        max_ef = max(max_ef, self.activities[pred_id].ef)
                act.es = max_ef
            act.ef = act.es + act.duration

        # Project Duration
        total_duration = max((act.ef for act in self.activities.values()), default=0)

        # Backward Pass
        for act_id in reversed(order):
            act = self.activities[act_id]
            if not act.successors:
                act.lf = total_duration
            else:
                min_ls = float('inf')
                for succ_id in act.successors:
                    if succ_id in self.activities:
                        min_ls = min(min_ls, self.activities[succ_id].ls)
                act.lf = min_ls if min_ls != float('inf') else total_duration
            act.ls = act.lf - act.duration
            act.tf = act.ls - act.es
            
            # Free Float
            if not act.successors:
                act.ff = total_duration - act.ef
            else:
                min_es_succ = min((self.activities[s].es for s in act.successors if s in self.activities), default=act.ef)
                act.ff = min_es_succ - act.ef
                
            act.is_critical = (act.tf == 0)

        critical_path = [act.id for act in self.activities.values() if act.is_critical]
        
        return {
            "total_duration": total_duration,
            "critical_path": critical_path,
            "activities": [act.to_dict() for act in self.activities.values()]
        }
