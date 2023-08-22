<template>
  <div id="app">
    <div id="content">
      <canvas ref="chart"></canvas>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js';

export default {
  name: 'app',
  props: {
    dataChart: {
      dateDBT: Array,
      dataMK_NEW: Array,
      dataCK1_NEW: Array,
      dataCK2_NEW: Array,
      dataCK2_CAN: Array,
      labelChart: Array,
      dateT: String,
      dateT1: String
    }
  },
  mounted() {
    var chart = this.$refs.chart;
    var ctx = chart.getContext('2d');
    var myChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: this.dataChart.labelChart,
        datasets: [
          {
            label: this.dataChart.dateT1,
            type: 'line',
            data: this.dataChart.dataMK_NEW,
            fill: false,
            backgroundColor: 'rgba(24,116,205)',
            borderColor: 'rgba(24,116,205)',
            borderWidth: 3
          },
          {
            label: this.dataChart.dateT,
            type: 'line',
            data: this.dataChart.dateDBT,
            fill: false,
            backgroundColor: 'rgba(255,140,0)',
            borderColor: 'rgba(255,140,0)',
            borderWidth: 3
          }
        ]
      },

      options: {
        legend: {
          position: 'bottom'
        },
        animation: {
          // duration: 1,
          onComplete: function() {
            // var chartInstance = this.chart,
            // ctx = chartInstance.ctx;
            // ctx.textAlign = 'center';
            // ctx.textBaseline = 'bottom';
            // ctx.font = "10px 'Helvetica Neue', Helvetica, Arial, sans-serif";
            // ctx.fillStyle = "#666";

            // // Loop through each data in the datasets

            // this.data.datasets.forEach(function (dataset, i) {
            //   var meta = chartInstance.controller.getDatasetMeta(i);
            //   meta.data.forEach(function (bar, index) {
            //     var data = dataset.data[index].toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            //     ctx.fillText(data, bar._model.x, bar._model.y);
            //   });
            // });
            var baseImageChart = myChart.toBase64Image();
            localStorage.setItem('baseImageChart1', baseImageChart);
          }
        },
        scales: {
          responsive: true,
          lineTension: 1,
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
                // padding: 25,
                callback: function(value, index, values) {
                  if (parseInt(value) >= 1000) {
                    return value
                      .toString()
                      .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                  } else {
                    return value
                      .toString()
                      .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                  }
                }
              }
            }
          ]
        },
        tooltips: {
          mode: 'index',
          intersect: false,
          callbacks: {
            label: function(context, data) {
              var datasetLabel =
                data.datasets[context.datasetIndex].label || '';
              return (
                datasetLabel +
                ':' +
                context.yLabel.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
              );
            }
          }
        },
        hover: {
          mode: 'index',
          intersect: false
          // animationDuration: 0
        }
      }
    });
  }
};
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 30px;
}
#content {
  margin: auto;
  min-width: 600px;
  background-color: #ffffff;
  padding: 20px 0px 20px 0px;
}
canvas {
  max-width: 700px;
}
</style>
