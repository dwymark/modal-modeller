# modal-modeller

This is an interactive web-based application for visual modeling and analysis of modal formulas and their models, namely, possible worlds models. This application allows users to create, modify, analyze, and visualize possible models, with a special focus on acyclicity, tree structures, and graph-theoretic reachability. It includes tools for verifying model properties, evaluating formulas, and generating new models or formulas, which can be useful in a variety of logical and mathematical applications.

I wrote this in early 2022 in collaboration with my friend Colin Bloomfield, in a joint effort to investigate the combinatoric properties of possible worlds models.

## Code Structure

- `com.danielwymark.cmmmodels.core.model`: Java classes for constructing and manipulating possible worlds models, including model building, minimization, and visualization.
- `com.danielwymark.cmmmodels.core.constraints`: Java classes for verifying model properties, such as acyclicity and tree structure, in addition to checking unreachability.
- `com.danielwymark.cmmmodels.core.evaluation`: Java classes for evaluating formulas in a modal model, supporting various logical operators and model-based checks.
- `com.danielwymark.cmmmodels.core.generator`: Interface for implementing custom object generators.
- `com.danielwymark.cmmmodels.core.paths`: Classes for generating and working with rooted paths in a world graph.
- `com.danielwymark.cmmmodels.core.syntax`: Classes for representing and generating logical formulas, including atomic formulas, compound formulas, and formula manipulation.
- `com.danielwymark.cmmmodels.webapp`: Web application for visualizing and analyzing computational models, featuring server handling, route definitions, and interactive functionalities.
