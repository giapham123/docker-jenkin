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
      dataCK2_CAN: Array
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
            label: 'MAKER',
            data: this.dataChart.dataMK_NEW,
            backgroundColor: ['rgba(0, 177, 255, 0.2)'],
            borderColor: ['rgba(0, 177, 255, 1)'],
            borderWidth: 1
          },
          {
            label: 'CHECKER1',
            data: this.dataChart.dataCK1_NEW,
            backgroundColor: ['rgba(160, 121, 242, 0.2)'],
            borderColor: ['rgba(160, 121, 242, 1)'],
            borderWidth: 1
          },
          {
            label: 'CHECKER2',
            data: this.dataChart.dataCK2_NEW,
            backgroundColor: ['rgba(127,205,98, 0.2)'],
            borderColor: ['rgba(127,205,98, 1)'],
            borderWidth: 1
          }
          // {
          //   label: 'CHECKER2_CANCEL',
          //   data: this.dataChart.dataCK2_CAN,
          //   backgroundColor: ['rgba(247, 218, 123, 0.2)'],
          //   borderColor: ['rgba(247, 218, 123, 1)'],
          //   borderWidth: 1
          // }
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
