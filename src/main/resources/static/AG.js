const columnDefs = [
    { field: "设备ID" },
    { field: "当前状态值" },
    { field: "时间1" },
    { field: "DI通道状况" },
    { field: "先前状态值" },
    { field: "时间2" },
    { field: "先前DI通道状况" },
    { field: "变化原因" },

];

// specify the data
const rowData = [
    { 设备ID: "FF", 当前状态值: "[0, 1, 1, 0, 0, 0, 0, 0]", DI通道状况: "DI-1通道: 断开  DI-2通道: 断开  DI-3通道: 断开  DI-4通道: 断开  DI-5通道: 断开  DI-6通道: 闭合  DI-7通道: 闭合  DI-8通道: 断开" },
    { make: "Ford", model: "Mondeo", price: 32000 },
    { make: "Porsche", model: "Boxster", price: 72000 }
];

// let the grid know which columns and what data to use
const gridOptions = {
    columnDefs: columnDefs,
    rowData: rowData
};

// setup the grid after the page has finished loading
document.addEventListener('DOMContentLoaded', () => {
    const gridDiv = document.querySelector('#myGrid');
    new agGrid.Grid(gridDiv, gridOptions);
});

const gridOptions = {
    columnDefs: columnDefs,
    rowData: data,
    onGridReady: function () {
        gridOptions.api.sizeColumnsToFit();
    },
    defaultColDef: {
        resizable: true,//是否开启调整列大小，就是拖动改变列大小
    }
};