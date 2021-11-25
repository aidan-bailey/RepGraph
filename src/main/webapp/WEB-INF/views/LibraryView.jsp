<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!-- HEAD -->
<%@include file="../../head.html" %>

<body>
    <!-- Main Container -->
    <div class="container-fluid h-100">
        <!-- Nav Bar -->
        <div class="row ten blue-container nav-main align-items-center p-2">
            <div class="col-4 mt-1">
                <h2>RepGraph</h2>
            </div>
            <div class="col-4 mx-auto">
                <div class="d-flex justify-content-center">
                    <h1>Graph Library</h1>
                </div>
            </div>
            <div class="col-4 mt-1">
                <ul>
                    <li><a href="landing">
                            <h5>File Options</h5>
                        </a></li>
                </ul>
            </div>
        </div>
        <!-- Main Page -->
        <div class="row ninety">
            <!-- Left hand column -->
            <div class="col-5 h-100 pt-4">
                <div class="container-fluid h-100">
                    <div class="row h-100">
                        <div class="col-md-12 box">
                            <!-- Table -->
                            <div class="tableWrapper">
                                <table id="myTable" class="table table-hover table-sm">
                                    <thead>
                                        <tr>
                                            <th>Id</th>
                                            <th>Sentence</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Right hand column -->
            <div class="col-7 h-100 pt-4">
                <div class="container-fluid h-100">
                    <div class="row h-100">
                        <div class="col-md-12 box">
                            <div class="row">
                                <div class="col-12">
                                    <div class="fa-lg fa-question-circle"
                                        style="margin-left: 10px; margin-top: 10px; float: right; font-family: fontawesome !important;"
                                        data-toggle="tooltip" data-placement="top"
                                        title="Click on a node to view its neighbourhood.">
                                    </div>
                                    <div class="dropdown">
                                        <button type="button"
                                            class="btn btn-outline-primary dropdown-toggle float-right"
                                            data-toggle="dropdown">
                                            Graph Options
                                        </button>
                                        <div class="dropdown-menu">
                                            <a id="key" class="dropdown-item" href="#" data-toggle="modal"
                                                data-target="#keyModal">View Key</a>
                                            <a id="btnReset" class="dropdown-item" href="#">Reset Graph</a>
                                            <a id="downloadBtn" class="dropdown-item" href="#">Download Graph as PNG</a>
                                            <a id="downloadSVGBtn" class="dropdown-item" href="#">Download Graph as
                                                SVG</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Graph Display -->
                            <div class="row h-75">
                                <div class="col-md-12">
                                    <div class="graphContainer h-100" id="graphDisplay">

                                    </div>
                                </div>
                            </div>
                            <!-- Tokens -->
                            <div class="row fixed">
                                <div class="container p-10" id="tokens"></div>
                            </div>
                            <!-- Bottom Panel -->
                            <div class="row fixed h-10 p-10">
                                <!-- Radio Buttons -->
                                <div class="offset-1 col-5">
                                    <div class="btn-group">
                                        <div class="form-check form-check-inline">
                                            <input disabled class="form-check-input" type="radio" id="inlineRadio1"
                                                name="optradio" value="Graph">
                                            <label class="form-check-label" for="inlineRadio1">DMRS graph only</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input disabled class="form-check-input" type="radio" id="inlineRadio2"
                                                name="optradio" value="Tokens">
                                            <label class="form-check-label" for="inlineRadio2">DMRS graph with
                                                tokens</label>
                                        </div>
                                    </div>
                                </div>
                                <!-- Analyse button -->
                                <div class="col-5">
                                    <a id="btnAnalysis" href="analysis" class="btn btn-primary disabled float-right">
                                        ANALYSE
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Modal View for Key -->
    <div class="modal" id="keyModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered pt-5" style="width: 300px" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Key</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" style="min-height: 200px;">
                    <div>
                        <span id="_surfaceNode" class="dot"></span>
                        <span>Surface Node</span>
                    </div>
                    <div>
                        <span id="_abstractNode" class="dot"></span>
                        <span>Abstract Node</span>
                    </div>
                    <div>
                        <span id="_topNode" class="dot"></span>
                        <span>Top Node</span>
                    </div>
                    <div>
                        <span id="_tokens" class="dot"></span>
                        <span>Tokens</span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</body>

<script>
    $(document).ready(function () {
        // LIBRARY EVENTS
        // Adds Data Table
        $("*").removeClass("disable-mouse");
        $("*").addClass("disable-mouse");
        var table = $("#myTable").DataTable({
            ajax: {
                url: "/library/browse",
                type: "GET",
                dataSrc: ""
            },
            columns: [
                { data: "id" },
                { data: "input" }
            ],
            scrollY: '50vh',
            scrollCollapse: false,
            select: 'single',
            info: true,
            paging: true
        });
        $("*").removeClass("disable-mouse");

        // When input selected, tokens shown in token display and graph shown in graph display
        var selectedId = "";
        $("#myTable tbody").on("click", "tr", function () {
            $("*").addClass("disable-mouse");
            var data = table.row(this).data();
            selectedId = data.id;
            $("input").removeAttr("disabled");
            $("#inlineRadio2").attr("checked", true);
            $("a").removeClass("disabled");
            displayGraph();
            $("*").removeClass("disable-mouse");
        });

        // DROPDOWN MENU EVENTS
        // Download  as PNG
        $("#downloadBtn").off("click").on("click", function () {
            downloadGraphPNG("s");
        });

        // Download graph as SVG
        $("#downloadSVGBtn").off("click").on("click", function () {
            downloadGraphSVG("s");
        });

        // Reset Button
        $("#btnReset").off("click").on("click", function () {
            displayGraph();
        });

        // GRAPH EVENTS
        var withToken = true;
        // Toggles Radio Options
        $('input[type="radio"]').click(function () {
            $("*").addClass("disable-mouse");
            var inputVal = $(this).attr("value");
            withToken = inputVal == "Tokens";
            displayGraph();
            $("*").removeClass("disable-mouse");
        });

        // Go to analysis page
        $("#btnAnalysis").off("click").on("click", "tr", function () {
            window.location.replace("/analysis");
        });

        /**
         * 
         * Displays the sigma graph in the graph container.
         * **/
        function displayGraph() {
            var link = "/library/select-graph/" + selectedId + "?withToken=" + withToken;
            $.ajax({
                url: link,
                type: "GET",
                success: function (result) {
                    $("#graphDisplay").empty();
                    destroyLibraryGraph();
                    createLibraryGraph(result);
                }
            });
        };
    });

</script>