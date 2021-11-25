<%@ page language='java' contentType='text/html; charset=ISO-8859-1' pageEncoding='ISO-8859-1'%>
<!-- HEAD -->
<%@include file='../../head.html' %>

<body>
    <!-- Main Container -->
    <div class='fluid-container'>
        <div class='col-12 mt-md-5'>
            <div class='row'>
                <!-- Sub Container -->
                <div class='offset-xl-3 col-xl-6 offset-lg-2 col-lg-8 col-md-10 offset-md-1 blue-container upload'>
                    <!-- Title -->
                    <div class='d-flex justify-content-center'>
                        <h1 style='font-size: 3vmax;'>RepGraph</h1>
                    </div>
                    <div class='d-flex justify-content-center mt-4'>
                        <h5>File Setup</h5>
                    </div>
                    <div class='d-flex justify-content-center mt-4'>
                        <a href="#" id='btnUpload' class='btn btn-primary' style='margin-top: 8px;'>
                            Upload File
                        </a>
                    </div>
                    <div class='d-flex justify-content-center mt-4'>
                        <a href="#" id='btnLoad' class='btn btn-primary disabled' style='margin-top: 8px;'>
                            Browse Saved Files
                        </a>
                    </div>
                    <div class='d-flex justify-content-center mt-4'>
                        <a href="#" id='btnCache' class='btn btn-primary disabled' style='margin-top: 8px;'>
                            Use Cached File
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class='modal fade' id='uploadModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalCenterTitle'
        aria-hidden='true'>
        <div class='modal-dialog modal-lg modal-dialog-centered' role='document'>
            <div class='modal-content'>
                <div class='modal-header'>
                    <h5 class='modal-title' id='exampleModalLongTitle'>File Uploader</h5>
                    <button type='button' class='close' data-dismiss='modal' aria-label='Close'>
                        <span aria-hidden='true'>&times;</span>
                    </button>
                </div>
                <div class='modal-body'>
                    <div class='container-fluid'>
                        <div id='fine-uploader-gallery' style='margin-top: 10px;'></div>
                    </div>
                </div>
                <div class='modal-footer'>
                    <input disabled id='chkSave' type='checkbox' class='form-check'>Save</input>
                    <input disabled id='txtFilename' type='text' class='form-control' style='margin-left: 12px;'
                        placeholder='filename'></input>
                    <label for='txtFilename' style='margin-right: 64px; margin-top: 12px;'>
                        <h5>.dmrs</h5>
                    </label>
                    <a href="#" id='btnReset' type='button' class='btn disabled btn-secondary'
                        style='margin-right: 8px;'>Reset</a>
                    <a href="#" id='btnContinue' type='button' class='btn disabled btn-primary'
                        style='margin-right: 8px;'>Continue</a>
                </div>
            </div>
        </div>
    </div>
    <div class='modal fade' id='loadModal' tabindex='-1' role='dialog' aria-labelledby='exampleModalCenterTitle'
        aria-hidden='true'>
        <div class='modal-dialog modal-dialog-centered' role='document'>
            <div class='modal-content'>
                <div class='modal-header'>
                    <h5 class='modal-title' id='exampleModalLongTitle'>Stored File List</h5>
                    <button type='button' class='close' data-dismiss='modal' aria-label='Close'>
                        <span aria-hidden='true'>&times;</span>
                    </button>
                </div>
                <div class='modal-body'>
                    <div class='container-fluid'>
                        <table id='tblLoad' class='table table-hover' width='100%'>
                            <thead>
                                <tr>
                                    <th>Filename</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <div class='modal-footer'>
                </div>
            </div>
        </div>
    </div>
</body>

<script>
    $(document).ready(function () {

        // UPLOADER
        var filename;
        var uploader = $('#fine-uploader-gallery').fineUploader({
            request: {
                endpoint: '/landing/upload',
                inputName: 'file'
            },
            multiple: false,
            thumbnails: {
                placeholders: {
                    waitingPath: '../../resources/plugins/fine-uploader/img/waiting-generic.png',
                    notAvailablePath: '../../resources/plugins/fine-uploader/img/not_available-generic.png'
                }
            },
            validation: {
                allowedExtensions: ['dmrs']
            },
            callbacks: {
                onComplete: function (id, name, responseJSON, xhr) {
                    $('#btnContinue').removeClass('disabled');
                    $('#chkSave').removeAttr('disabled');
                    $('#btnReset').removeClass('disabled');
                    filename = name.split('.').slice(0, -1).join('.');
                    $('#txtFilename').val(filename);
                },
                onDelete: function (id, xhr, isError) {
                    $('#btnContinue').addClass('disabled');
                    $('#chkSave').attr('disabled', true);
                    $('#txtFilename').val('');
                    $('#txtFilename').attr('disabled', true);
                }
            }
        });

        // DMRS SELECTOR
        var filenames = ${ savedFilenames };
        if (filenames.length > 0) {
            $('#btnLoad').removeClass('disabled');
        }

        var table = $('#tblLoad').DataTable({
            data: filenames,
            dom: 'Blfrtip',
            paging: false,
            scrolly: '30vh',
            scrollCollapse: true,
            select: true,
            columns:
                [{ title: 'Filename' }],
            info: false,
            searching: false,
            buttons: [
                {
                    text: 'Load File',
                    action: function (e, dt, node, config) {
                        $("*").addClass("disable-mouse");
                        var filename = dt.rows({ selected: true }).data()[0][0];
                        var request = new XMLHttpRequest();
                        request.open('GET', '/landing/load/' + filename);
                        request.onload = function () {
                            var data = JSON.parse(this.response);
                            if (data.success === true) {
                                window.location.replace('/library');
                            }
                            else {
                                $("*").removeClass("disable-mouse");
                                alert('Error: unable to load specified file: ' + filename);
                            }
                        }
                        request.send();
                    },
                    enabled: false
                },
                {
                    text: 'Delete File',
                    action: function (e, dt, node, config) {
                        $("*").addClass("disable-mouse");
                        var filename = dt.rows({ selected: true }).data()[0];
                        var request = new XMLHttpRequest();
                        request.open('GET', '/landing/delete/' + filename);
                        request.onload = function () {
                            var data = JSON.parse(this.response);
                            if (data.success === true) {
                                $.ajax({
                                    url: '/landing/stored',
                                    success: function (data) {
                                        var response = JSON.parse(data);
                                        table.clear();
                                        table.rows.add(response)
                                        table.draw();
                                        if (table.data().count() === 0) {
                                            $('#btnLoad').addClass('disabled');
                                        }
                                        table.button(0).enable(false);
                                        table.button(1).enable(false);
                                    }
                                });
                            }
                        };
                        request.send();
                        $("*").removeClass("disable-mouse");
                    },
                    enabled: false
                }
            ]
        });

        // TABLE ONSELECT LISTENER

        table.on('select deselect', function () {
            var selectedRows = table.rows({ selected: true }).count();
            table.button(0).enable(selectedRows === 1);
            table.button(1).enable(selectedRows > 0);
        });


        // BUTTON LISTENERS

        $('#btnLoad').off('click').on('click', function () {
            $('#loadModal').modal('toggle');
        });

        $('#btnUpload').off('click').on('click', function () {
            $('#uploadModal').modal('toggle');
        });

        var hasCache = ${ cached };
        if (hasCache) { $('#btnCache').removeClass('disabled'); }
        $('#btnCache').off('click').on('click', function () {
            $("*").addClass("disable-mouse");
            var request = new XMLHttpRequest();
            request.open('GET', '/landing/loadCache');
            request.onload = function () {
                var data = JSON.parse(this.response);
                if (data.success === false) {
                    $("*").removeClass("disable-mouse");
                    alert('Failure when saving file!')
                }
                else {
                    window.location.replace('/library');
                }
            };
            request.send();
        });

        $('#chkSave').off('click').on('click', function () {
            if ($(this).prop('checked') == true) {
                $('#txtFilename').removeAttr('disabled');
            }
            else if ($(this).prop('checked') == false) {
                $('#txtFilename').val(filename);
                $('#txtFilename').attr('disabled', true);
            }
        });

        $('#btnContinue').off('click').on('click', function () {
            $("*").addClass("disable-mouse");
            var save = $('#chkSave').prop('checked') == true;
            if (save) {
                var filename = $('#txtFilename').val();
                var request = new XMLHttpRequest();
                request.open('GET', '/landing/save/' + filename + '.dmrs');
                request.onload = function () {
                    var data = JSON.parse(this.response);
                    if (data.success === false) {
                        $("*").removeClass("disable-mouse");
                        alert('Failure when saving file!')
                    }
                };
                request.send();
            }
            var request = new XMLHttpRequest();
            request.open('GET', '/landing/loadUpload');
            request.onload = function () {
                var data = JSON.parse(this.response);
                if (data.success === true) {
                    window.location.replace('/library');
                }
                else {
                    $("*").removeClass("disable-mouse");
                    alert('Uploaded file parse failure!')
                }
            }
            request.send();
        });

        $('#btnReset').off('click').on('click', function () {
            $("*").addClass("disable-mouse");
            $('#fine-uploader-gallery').fineUploader('reset');
            $.ajax({
                url: '/landing/cancelUpload', success: function () {
                    $('#btnContinue').addClass('disabled');
                    $('#chkSave').attr('disabled', true);
                    $('#txtFilename').val('');
                    $('#txtFilename').attr('disabled', true);
                    $('#btnReset').addClass('disabled');
                }
            });
            $("*").removeClass("disable-mouse");
        });
    });
</script>

<!-- FILE UPLOADER TEMPLATE -->
<%@include file='../../resources/html.snippets/fineuploader-gallery.html' %>