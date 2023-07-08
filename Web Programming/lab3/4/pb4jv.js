function bubbleSort(arr, indexArray, isNumeric) {
    let sorted = false;
    while (!sorted) {
        sorted = true;
        for (let i = 0; i < arr.length - 1; i++) {
            let el1 = arr[i].innerHTML;
            let el2 = arr[i + 1].innerHTML;

            if (isNumeric ? parseInt(el1) > parseInt(el2) : el1 > el2) {
                [arr[i], arr[i + 1]] = [arr[i + 1], arr[i]];
                [indexArray[i], indexArray[i + 1]] = [indexArray[i + 1], indexArray[i]];
                sorted = false;
            }
        }
    }
}

function sort(columnHeader) {
    var sortCriteria = columnHeader.innerHTML;
    var table = columnHeader.parentNode.parentNode.parentNode;
    let toSort = [];
    for (var i = 0, row; row = table.rows[i]; i++) {
        if (row.cells[0].innerHTML == sortCriteria) {
            toSort = [].slice.call(row.cells);
            toSort = toSort.slice(1, toSort.length);
        }
    }

    
    var indexArray = [];
    for (let i = 0; i < toSort.length; i++) {
        indexArray.push(i);
    }

    let isNumeric = !isNaN(toSort[0].innerHTML);
    bubbleSort(toSort, indexArray, isNumeric);

    for (var i = 0, row; row = table.rows[i]; i++) {
        let sortRow = new Array(row.cells.length);
        for (var j = 1; j < row.cells.length; j++) {
            sortRow[j] = row.cells[indexArray[j - 1] + 1].innerHTML;
        }
        for (var j = 1; j < sortRow.length; j++) {
            row.cells[j].innerHTML = sortRow[j];
        }
    }
}









function sort2(columnHeader) {
    var columnIndex = columnHeader.cellIndex;
    var table = columnHeader.parentNode.parentNode;
    let toSort = [];

    for (var i = 1; i < table.rows.length; i++) {
        toSort.push(table.rows[i].cells[columnIndex]);
    }
    
    var indexArray = [...Array(toSort.length).keys()];
    let isNumeric = !isNaN(toSort[0].innerHTML);
    bubbleSort(toSort, indexArray, isNumeric);

    let sortedRows = [];
    for (var i = 0; i < indexArray.length; i++) {
        sortedRows.push(table.rows[indexArray[i] + 1].cloneNode(true));
    }

    for (var i = 1; i < table.rows.length; i++) {
        for (var j = 0; j < sortedRows[i - 1].cells.length; j++) {
            table.rows[i].cells[j].innerHTML = sortedRows[i - 1].cells[j].innerHTML;
        }
    }
}


