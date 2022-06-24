"use strict";

var params = null;
var colsEdi = null;
var newColHtml = '<div class="btn-group pull-right">' +
    '<button id="bEdit" type="button" class="btn btn-sm btn-default" onclick="rowEdit(this);">' +
    '<span class="glyphicon glyphicon-pencil" > </span>' +
    '</button>' +
    '<button id="bElim" type="button" class="btn btn-sm btn-default" onclick="rowElim(this);">' +
    '<span class="glyphicon glyphicon-trash" > </span>' +
    '</button>' +
    '<button id="bAcep" type="button" class="btn btn-sm btn-default" style="display:none;" onclick="rowAcep(this);">' +
    '<span class="glyphicon glyphicon-ok" > </span>' +
    '</button>' +
    '<button id="bCanc" type="button" class="btn btn-sm btn-default" style="display:none;" onclick="rowCancel(this);">' +
    '<span class="glyphicon glyphicon-remove" > </span>' +
    '</button>' +
    '</div>';
var colEdicHtml = '<td name="buttons">' + newColHtml + '</td>';

$.fn.SetEditable = function (options) {
    var defaults = {
        columnsEd: null,
        $addButton: null,
        onEdit: function () {
        },
        onBeforeDelete: function () {
        },
        onDelete: function () {
        },
        onAdd: function () {
        }
    };
    params = $.extend(defaults, options);
    this.find('thead tr').append('<th name="buttons"></th>');
    this.find('tbody tr').append(colEdicHtml);
    var $tabedi = this;

    if (params.$addButton != null) {

        params.$addButton.click(function () {
            rowAddNew($tabedi.attr("id"));
        });
    }

    if (params.columnsEd != null) {
        colsEdi = params.columnsEd.split(',');
    }
};

function IterarCamposEdit($cols, tarea) {
    //console.log("IterarCamposEdit=================================>" + $cols + tarea);

    var n = 0;
    $cols.each(function () {
        n++;
        if ($(this).attr('name') == 'buttons') return;
        if (!EsEditable(n - 1)) return;
        tarea($(this));
    });

    function EsEditable(idx) {
        //console.log("EsEditable=================================>" + idx);

        if (colsEdi == null) {
            return true;
        } else {
            for (var i = 0; i < colsEdi.length; i++) {
                if (idx == colsEdi[i]) return true;
            }
            return false;
        }
    }
}

function FijModoNormal(but) {
    //console.log("FijModoNormal=================================>" + but);
    $(but).parent().find('#bAcep').hide();
    $(but).parent().find('#bCanc').hide();
    $(but).parent().find('#bEdit').show();
    $(but).parent().find('#bElim').show();
    var $row = $(but).parents('tr');
    $row.attr('id', '');
}

function FijModoEdit(but) {
    //console.log("FijModoEdit=================================>" + but);
    $(but).parent().find('#bAcep').show();
    $(but).parent().find('#bCanc').show();
    $(but).parent().find('#bEdit').hide();
    $(but).parent().find('#bElim').hide();
    var $row = $(but).parents('tr');
    $row.attr('id', 'editing');
}

function ModoEdicion($row) {
    //console.log("ModoEdicion=================================>" + $row);
    if ($row.attr('id') == 'editing') {
        return true;
    } else {
        return false;
    }
}

function rowAcep(but) {
    //submitData(but);
    //console.log("rowAcep=================================>" + but);

    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (!ModoEdicion($row)) return;

    IterarCamposEdit($cols, function ($td) {
        var cont = $td.find('input').val();
        $td.html(cont);
    });
    FijModoNormal(but);
    params.onEdit($row);
}

function rowCancel(but) {
    //console.log("rowCancel=================================>" + but);

    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (!ModoEdicion($row)) return;

    IterarCamposEdit($cols, function ($td) {
        var cont = $td.find('div').html();
        $td.html(cont);
    });
    FijModoNormal(but);
}

function rowEdit(but) {
    //console.log("rowEdit=================================>" + but);
    var $row = $(but).parents('tr');
    var $cols = $row.find('td');
    if (ModoEdicion($row)) return;

    IterarCamposEdit($cols, function ($td) {
        var cont = $td.html();
        var div = '<div style="display: none;">' + cont + '</div>';
        var input = '<input class="form-control input-sm"  value="' + cont + '">';
        $td.html(div + input);
    });
    FijModoEdit(but);
}

function rowElim(but) {
    //console.log("rowElim=================================>" + but);
    var $row = $(but).parents('tr');
    params.onBeforeDelete($row);
    $row.remove();
    params.onDelete();
}

function rowAddNew(tabId) {
    //console.log("rowAddNew=================================>" + tabId);
    var $tab_en_edic = $("#" + tabId);
    var $filas = $tab_en_edic.find('tbody tr');
    if ($filas.length == 0) {

        var $row = $tab_en_edic.find('thead tr');
        var $cols = $row.find('th');

        var htmlDat = '';
        $cols.each(function () {
            if ($(this).attr('name') == 'buttons') {
                htmlDat = htmlDat + colEdicHtml;
            } else {
                htmlDat = htmlDat + '<td></td>';
            }
        });
        $tab_en_edic.find('tbody').append('<tr>' + htmlDat + '</tr>');
    } else {

        var $ultFila = $tab_en_edic.find('tr:last');
        $ultFila.clone().appendTo($ultFila.parent());
        $ultFila = $tab_en_edic.find('tr:last');
        var $cols = $ultFila.find('td');
        $cols.each(function () {
            if ($(this).attr('name') == 'buttons') {

            } else {
                $(this).html('');
            }
        });
    }
    params.onAdd();
}

function TableToCSV(tabId, separator) {
    //console.log("TableToCSV=================================>" + tabId + separator);
    var datFil = '';
    var tmp = '';
    var $tab_en_edic = $("#" + tabId);
    $tab_en_edic.find('tbody tr').each(function () {

        if (ModoEdicion($(this))) {
            $(this).find('#bAcep').click();
        }
        var $cols = $(this).find('td');
        datFil = '';
        $cols.each(function () {
            if ($(this).attr('name') == 'buttons') {

            } else {
                datFil = datFil + $(this).html() + separator;
            }
        });
        if (datFil != '') {
            datFil = datFil.substr(0, datFil.length - separator.length);
        }
        tmp = tmp + datFil + '\n';
    });
    return tmp;
}
