/*Shared Variables*/
var nodes, edges; // to be filled with data on nodes and edges for all sigma instances
var s, modalGraph, compareGraph; // all sigma instances
var selectedNode = "", subgraphBtnSelected = false, nodeIds = []; // used by various analysis tools
var defaultColor = '#bababa'; // default colour of nodes and edges - a very light grey
var selectedColor = '#e66140'; // bright red/orange used to highlight specific nodes

/**
 * Method that finds and returns the adjacent edges of a selected node.
 */
sigma.classes.graph.addMethod('attachedEdges', function (nodeId) {
  var adjEdges = [],
    index = this.allNeighborsIndex[nodeId];
  for (n in index) {
    for (e in index[n]) {
      adjEdges.push(index[n][e]);
    }
  }
  return adjEdges;
});

/**
 * Sigma Instantiation with settings to be used across all views.
 */
s = new sigma ({
    settings: {
      labelThreshold: 0,
      minArrowSize: 10,
      sideMargin: 1,
      doubleClickEnabled: false
    }
});

/**
 * Sigma Instantiation of modal graph
 */
modalGraph = new sigma({
  settings: {
    labelThreshold: 0,
    minArrowSize: 10,
    doubleClickEnabled: false
  }
});

/**
 * Sigma Instantiation of compare graph 
 */
compareGraph = new sigma({
  settings: {
    labelThreshold: 0,
    minArrowSize: 10,
    doubleClickEnabled: false
  }
});

/**
 * Creates a Sigmajs graph specifically for the LibraryView.
 * @param {JsonString} result 
 */
function createLibraryGraph(result) {

  var jsonGraph = JSON.parse(result);

  s.addRenderer({
    container: document.getElementById('graphDisplay'),
    type: sigma.renderers.canvas
  });

  s.graph.read(jsonGraph);

  nodes = s.graph.nodes();
  edges = s.graph.edges();

  // Customize edges
  customizeEdges();
  s.refresh();

  // Custom Sigma Events for Library View
  customizeLibraryEvents();
}

/**
 * Gives all edges the arrow type - making the graph directed.
 * Gives the token edges a specific colour, makes the edges dashed and hides them by default.
 */
function customizeEdges() {
  for (i = 0; i < edges.length; i++) {
    edges[i].type = 'arrow'; // Add arrows to all edges
    if (edges[i].target.startsWith("t")) {
      // Change colour, visibility and type of token edges
      edges[i].hidden = true;
      edges[i].type = 'dashed';
      edges[i].color = selectedColor;
    }
  }
}

/**
 * Binds events for the sigma instance on the library page. 
 * 1. Click a node to display the neighbourhood of the node (what nodes are connected to it)
 * 2. Hover over a node to display its token span.
 */
function customizeLibraryEvents() {
  s.bind("overNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = s.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = false;
      }
    }
    s.refresh();
  });

  s.bind("clickNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = s.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = false;
      }
    }
    for (j = 0; j < nodes.length; j++) {
      nodes[j].hidden = true;
    }

    e.data.node.hidden = false;

    if (adjEdges.length > 0) {
      for (i = 0; i < adjEdges.length; i++) {
        for (j = 0; j < nodes.length; j++) {
          if (adjEdges[i].target == nodes[j].id || adjEdges[i].source == nodes[j].id) {
            nodes[j].hidden = false;
          }
        }
      }
    }
    s.refresh();
  });

  s.bind("outNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = s.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = true;
      }
    }
    s.refresh();
  });

  s.bind("clickStage", function (e) {
    for (j = 0; j < nodes.length; j++) {
      nodes[j].hidden = false;
    }
    s.refresh();
  });
}

/**
 * Fills a created instance of sigma for the analysis page. Either fills s (the main instance), modalGraph (the instance in most modal views)
 * or compareGraph (the instance of the selected graph in the compare two graphs 'compareViewModal'.). These instances are filled with data
 * from result and are initialised in the container/div specified by view.
 * @param {JSON String from the backend containing graph data} result 
 * @param {ID of container the graph will be visualised in} view 
 */
function createAnalysisGraph(result, view) {
  var jsonGraph = JSON.parse(result);

  // creates instance for main analysis page
  if (view == "graphDisplay") {
    s.graph.read(jsonGraph);

    nodes = s.graph.nodes();
    edges = s.graph.edges();
    s.addRenderer({
      container: document.getElementById(view),
      type: sigma.renderers.canvas
    });
    s.settings({
      labelColor: 'node',
      defaultEdgeLabelColor: defaultColor,
      edgeColor: defaultColor
    });
    customizeEdges();
    if (subgraphBtnSelected){
      showTokens();
    }
    else {
      s.unbind("overNode");
      s.unbind("outNode");
    }
    s.refresh();
  }

  // creates instances for the different modal graph displays
  if (view == "modalGraphDisplay" || view == "propertiesModalGraph" || view == "thisGraphDisplay") {
    modalGraph.graph.read(jsonGraph);
    modalGraph.settings({
      defaultNodeColor: defaultColor,
      defaultEdgeLabelColor: defaultColor,
      labelColor: 'node'
    });
    modalGraph.addRenderer({
      container: document.getElementById(view),
      type: sigma.renderers.canvas
    });

    nodes = modalGraph.graph.nodes();
    edges = modalGraph.graph.edges();

    if (view == "propertiesModalGraph") {  // hide tokens initially for the property view
      for (i = 0; i < nodes.length; i++) {
        if (nodes[i].id.startsWith("t")) {
          nodes[i].hidden = true;
        }
      }
      modalGraph.settings({
        zoomingRatio: 1
      });
    }
    else if (view == "thisGraphDisplay") { // show tokens for graph comparison but hide edges and don't bind token events
      customizeEdges();
    }
    else { // for subgraph patterns show tokens with token events
      showTokens();
    }
    modalGraph.refresh();
  }

    // creates instance for the compare graph display - needed as there are more than one sigma instances on the same modal view
    if (view == "selectedGraphDisplay"){ // for the selected graph in the comparison view
      compareGraph.graph.read(jsonGraph);
      compareGraph.settings({
          defaultNodeColor: defaultColor,
          defaultEdgeLabelColor: defaultColor,
          labelColor: 'node'
      });
      
      compareGraph.addRenderer({
        container: document.getElementById(view),
        type: sigma.renderers.canvas
      });

      nodes = compareGraph.graph.nodes();
      edges = compareGraph.graph.edges();
      
      showTokens();
      compareGraph.refresh();
    }
}

/**
 * Allows tokens to be visualised and binds token events for added interactivity. When you hover over a token it displays
 * what nodes have that token in their span and when you hover over a node it displays what tokens are in their span.
 */
function showTokens() {
  for (i = 0; i < nodes.length; i++) {
    if (nodes[i].id.startsWith("t")) {
      nodes[i].hidden = false;
    }
  }
  for (i = 0; i < edges.length; i++) {
    if (edges[i].target.startsWith("t")) {
      // Change colour, visibility and type of token edges
      edges[i].hidden = true;
      edges[i].type = 'dashed';
      edges[i].color = selectedColor;
    }
  }
  modalGraph.refresh();
  customizeAnalysisEvents();
}

/**
 * Used in Graph Properties Modal. Displays surface nodes by highlighting them a bright green.
 */
function showSurfaceNodes() {
  for (i = 0; i < nodes.length; i++) {
    if (nodes[i].label.startsWith("_")) { // Surface nodes start with _
      nodes[i].color = "#40e661";
    }
  }
  modalGraph.refresh();
}

/**
 * Used in Graph Properties Modal. Displays abstract nodes by highlighting them a bright blue.
 */
function showAbstractNodes() {
  for (i = 0; i < nodes.length; i++) {
    if (!nodes[i].label.startsWith("_") && !nodes[i].id.startsWith("t")) {
      nodes[i].color = "#40c5e6";
    }
  }
  modalGraph.refresh();
}

/**
 * Used in Graph Properties Modal. Displays the top node by highlighting it a bright pink.
 */
function showTopNode() {
  for (i = 0; i < nodes.length; i++) {
    if (nodes[i].size == 2) {
      nodes[i].color = "#e640c5";
    }
  }
  modalGraph.refresh();
}

/**
 * Binds the hover events that, when you hover over a token it displays what nodes have that token in their span and 
 * when you hover over a node it displays what tokens are in their span.
 */
function customizeAnalysisEvents() {
  modalGraph.bind("overNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = modalGraph.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = false;
      }
    }
    modalGraph.refresh();
  });

  modalGraph.bind("outNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = modalGraph.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = true;
      }
    }
    modalGraph.refresh();
  });

  s.bind("overNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = s.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = false;
      }
    }
    s.refresh();
  });

  s.bind("outNode", function (e) {
    var nodeID = e.data.node.id;
    var adjEdges = s.graph.attachedEdges(nodeID);
    for (i = 0; i < adjEdges.length; i++) {
      if (adjEdges[i].id.startsWith("t")) {
        adjEdges[i].hidden = true;
      }
    }
    s.refresh();
    });

    compareGraph.bind("overNode", function (e) {
      var nodeID = e.data.node.id;
      var adjEdges = compareGraph.graph.attachedEdges(nodeID);
      for (i = 0; i < adjEdges.length; i++) {
        if (adjEdges[i].id.startsWith("t")) {
          adjEdges[i].hidden = false;
        }
      }
      compareGraph.refresh();
    });
  
    compareGraph.bind("outNode", function (e) {
      var nodeID = e.data.node.id;
      var adjEdges = compareGraph.graph.attachedEdges(nodeID);
      for (i = 0; i < adjEdges.length; i++) {
        if (adjEdges[i].id.startsWith("t")) {
          adjEdges[i].hidden = true;
        }
      }
      compareGraph.refresh();
  });
}

/**
 * Binds click events to the s, the sigma instance of the main container. If you're in the default view and you click a node, then
 * you can view its span, other nodes connected to tokens in its span and what tokens they're connected to. Once you click a node 
 * in the default view, you will also be able to view the subgraph generated by the selected node. If you opt to do so, it will change
 * to the subgraph view in which whenever you click on a node, it will display the nodes subgraph. To get back to the default view from
 * the subgraph view simply click the stage.
 */
function selectNode() {
  s.unbind("clickNode");
  s.bind("clickNode", function (e) {
    selectedNode = e.data.node.id;
    if (!selectedNode.startsWith("t")) {
      for (i = 0; i < nodes.length; i++) {
        nodes[i].color = defaultColor;
      }

      console.log("Selected Node: " + selectedNode);
      if (subgraphBtnSelected) {
        $('#clearSubgraphBtn').removeClass('disabled');
        var link = "/analysis/getSubgraphViz/" + selectedNode;
        $.ajax({
          url: link,
          type: "GET",
          success: function (result) {
            $("#graphDisplay").empty();
            destroyAnalysisGraph();
            createAnalysisGraph(result, "graphDisplay");
          }
        });
      }
      else {
        $("#subgraphBtn").removeClass("disabled");
        for (i = 0; i < nodes.length; i++) {
          if (!nodes[i].id.startsWith("t")) { // hide all the nodes that aren't token nodes
            nodes[i].hidden = true;
            if (nodes[i].id == selectedNode) { // show current node and its span
              nodes[i].color = selectedColor;
              nodes[i].hidden = false;
            }
          }
        }
        var span = []; // list showing a node's span
        var adjEdges = s.graph.attachedEdges(selectedNode); // get all edges of selected node
        if (adjEdges.length > 0) {
          for (i = 0; i < adjEdges.length; i++) {
            for (j = 0; j < nodes.length; j++) {
              if (adjEdges[i].source == nodes[j].id && adjEdges[i].target.startsWith("t")) {
                span.push(adjEdges[i].target); // add id of tokens to a list called span
              }
            }
          }
        }
        var nodesWithSameSpan = []; // list showing all nodes with same span
        if (span.length > 0) {
          for (i = 0; i < span.length; i++) {
            var adjSpanEdges = s.graph.attachedEdges(span[i]);
            if (adjSpanEdges.length > 0) {
              for (j = 0; j < adjSpanEdges.length; j++) {
                nodesWithSameSpan.push(adjSpanEdges[j].source);
              }
            }
          }
        }
        if (nodesWithSameSpan.length > 0) {
          for (i = 0; i < nodes.length; i++) {
            for (j = 0; j < nodesWithSameSpan.length; j++) {
              if (nodesWithSameSpan[j] == nodes[i].id) {
                nodes[i].hidden = false;
              }
            }
          }
        }

        for (i = 0; i < edges.length; i++) {
          if (edges[i].id.startsWith("t")) { // unhide token edges
            edges[i].hidden = false; // un hide all token edges
            // hide token edges who's token (target) is not in the span list
            if (span.length > 0) {
              if (!span.includes(edges[i].target)){
                edges[i].hidden = true;
              }
            }
          }
        }
      }
    }
    else if (selectedNode.startsWith("t") && !subgraphBtnSelected){
      for (i = 0; i < nodes.length; i++) {
        nodes[i].color = defaultColor;
        if (nodes[i].hidden) {
          nodes[i].hidden = false;
        }
      }
      for (i = 0; i < edges.length; i++){
        if(edges[i].id.startsWith("t")){
          edges[i].hidden = true;
        }
      }
      var adjEdges = s.graph.attachedEdges(selectedNode);
      for (i = 0; i < adjEdges.length; i++) {
        if (adjEdges[i].id.startsWith("t")) {
          adjEdges[i].hidden = false;
        }
      }
    }
    s.refresh();
  });

  s.unbind("clickStage");
  s.bind("clickStage", function (e) {
    s.unbind("overNode");
    s.unbind("outNode");
    $("#subgraph-header").removeAttr("style");
    $("#subgraphPattern-header").removeAttr("style");
    $("#modeHeader").html('Mode: Single Selection')

    selectedNode = "";
    subgraphBtnSelected = false;
    $("#subgraphBtn").css('display', 'block');
    $("#clearSubgraphBtn").css('display', 'none');
    $("#subgraphBtn").addClass("disabled");
    $("#patternMatchingBtn").addClass("disabled");
    console.log("Stage clicked. Selected node reset.");
    for (i = 0; i < nodes.length; i++) {
      nodes[i].color = defaultColor;
      if (nodes[i].hidden) {
        nodes[i].hidden = false;
      }
    }
    for (i = 0; i < edges.length; i++) {
      if (!edges[i].id.startsWith("t")) {
        edges[i].color = defaultColor;
      }
      else {
        edges[i].hidden = true;
      }
    }
    s.refresh();
  });
}

/**
 * Binds click events for nodes to s, the sigma instance of the main container. This unbinds previous click node events for the purpose
 * of being able to select a group of nodes and search other graphs for matching node labels. Once you click a node, the node label is 
 * added to a list of node labels which are sent to the backend for searching and matching. You will be alerted if you select a duplicate
 * node label or once you select the stage - selecting the stage will indicate that you are done selecting nodes to be matched.
 */
function selectNodeGroup() {
  for (i = 0; i < nodes.length; i++) {
    nodes[i].color = defaultColor;
  }
  for (i = 0; i < edges.length; i++) {
    if (!edges[i].id.startsWith("t")) {
      edges[i].color = defaultColor;
    }
  }
  s.unbind("clickNode");
  s.unbind("clickStage");
  modalGraph.unbind("clickNode");
  modalGraph.unbind("clickStage");

  s.bind("clickNode", function (e) {
    if (!nodeIds.includes(e.data.node.label) && !e.data.node.id.startsWith("t")) {
      nodeIds.push(e.data.node.label);
      console.log(nodeIds);
      $('#nodeMatchingBtn').css('display', 'none');
      $('#nodeSearchingBtn').css('display', 'block');
      $('#nodeSearchingBtn').html('Compute Search ' + '(' +nodeIds.length+ ' Node'+(nodeIds.length === 1?')':'s)'));
      for (i = 0; i < nodes.length; i++) {
        if (nodes[i].label == e.data.node.label) {
          nodes[i].color = selectedColor;
        }
      }
      s.refresh();
    }
    else {
      alert("The label you have selected is already in the list.");
    }
  });
  // When a user clicks the stage, they exit the option to select multiple nodes and exit the node pattern matching view.
  s.bind("clickStage", function (e) {
    var r = confirm("You have clicked off the nodes and are about to clear the current selection and exit the node matches tool. Continue?");
    if (r == false) {
      return
    }
    nodeIds = [];
    for (i = 0; i < nodes.length; i++) {
      nodes[i].color = defaultColor;
    }
    s.refresh();
    $('#nodeMatchingBtn').css('display', 'block');
    $('#nodeSearchingBtn').css('display', 'none');
    $("#nodePattern-header").removeAttr("style");
    $('#modeHeader').html('Mode: Single Selection');

    showSubgraph = false;
    selectNode();
  });
}

/**
 * While showing cut vertices, user can select a marked node and it will hide the node and its edges.
 */
function showCutVertices() {
  modalGraph.unbind("clickNode");
  modalGraph.unbind("clickStage");

  modalGraph.bind("clickNode", function (e) {

    for (i = 0; i < nodes.length; i++) {
      if (nodes[i].hidden == true && !nodes[i].id.startsWith("t")) {
        nodes[i].hidden = false;
      }
      if (e.data.node.id == nodes[i].id && nodes[i].color == "#a93316") {
        e.data.node.hidden = true;
      }
    }

    modalGraph.refresh();
  });

  modalGraph.bind("clickStage", function (e) {
    for (i = 0; i < nodes.length; i++) {
      if (nodes[i].hidden == true && !nodes[i].id.startsWith("t")) {
        nodes[i].hidden = false;
      }
    }
    modalGraph.refresh();
  });
}

/**
 * Downloads the current sigma instance as a png.
 * @param {String representation of the sigma instance to be downloaded} siginst 
 */
function downloadGraphPNG(siginst) {
  if (siginst == "s") {
    console.log("Downloading Graph");
    s.renderers[0].snapshot({ download: 'true', format: 'png', background: 'white', filename: 'my-graph.png', labels: true });
  }
  if (siginst == "modalGraph") {
    console.log("Downloading Graph");
    modalGraph.renderers[0].snapshot({ download: 'true', format: 'png', background: 'white', filename: 'my-graph.png', labels: true });
  }
  if (siginst == "compareGraph") {
    console.log("Downloading Graph");
    compareGraph.renderers[0].snapshot({ download: 'true', format: 'png', background: 'white', filename: 'my-graph.png', labels: true });
  }
}

/**
 * Downloads the current sigma instance as a svg.
 * @param {String representation of the sigma instance to be downloaded} siginst 
 */
function downloadGraphSVG(siginst) {
  if (siginst == "s") {
    console.log("Downloading Graph");
    s.toSVG({ download: true, filename: 'mygraph.svg', size: 1000, labels: true});
  }
  if (siginst == "modalGraph") {
    console.log("Downloading Graph");
    modalGraph.toSVG({ download: true, filename: 'mygraph.svg', size: 1000, labels: true });
  }
  if (siginst == "compareGraph") {
    console.log("Downloading Graph");
    compareGraph.toSVG({ download: true, filename: 'mygraph.svg', size: 1000, labels: true });
  }
}

/**
 * Clears an instance of Sigma of its graphs and edges
 */
function destroyLibraryGraph() {
  s.graph.clear();
}

/**
 * Clears an instance of Sigma of its graphs and edges
 */
function destroyAnalysisGraph() {
  s.graph.clear();
}

/**
 * Clears an instance of Sigma of its graphs and edges
 */
function destroyModalGraph() {
  modalGraph.graph.clear();
}

/**
 * Clears an instance of Sigma of its graphs and edges
 */
function destroyCompareGraph() {
  compareGraph.graph.clear();
}

