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
      dataSuccess: Array,
      dateDBT: Array,
      dataFail: Array,
      dataInProcess: Array
    }
  },
  mounted() {
    var chart = this.$refs.chart;
    var ctx = chart.getContext('2d');
    var myChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: this.dataChart.dateDBT,
        datasets: [
          {
            label: 'IMPORT_FAIL',
            data: this.dataChart.dataFail,
            backgroundColor: ['rgba(255, 99, 132, 0.2)'],
            borderColor: ['rgba(255,99,132,1)'],
            borderWidth: 1
          },
          {
            label: 'IMPORT_SUCCESS',
            data: this.dataChart.dataSuccess,
            backgroundColor: ['rgba(127,205,98, 0.2)'],
            borderColor: ['rgba(127,205,98, 1)'],
            borderWidth: 1
          },
          {
            label: 'IMPORT_IN_PROCESS',
            data: this.dataChart.dataInProcess,
            backgroundColor: ['rgba(251,215,0, 0.2)'],
            borderColor: ['rgba(251,215,0, 1)'],
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              }
            }
          ]
        },
        tooltips: {
          mode: 'index',
          intersect: false
        },
        hover: {
          mode: 'index',
          intersect: false
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
  margin-top: 60px;
}
#content {
  margin: auto;
  width: 600px;
  background-color: #ffffff;
  padding: 20px 0px 20px 0px;
}
canvas {
  max-width: 700px;
}
</style>
