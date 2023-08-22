<template>
  <v-container
    class="home-app-wrapper"
    grid-list-md
    fluid
    text-xs-center
    style="height:550px"
  >
    <v-card>
      <v-layout row style="height: 100%">
        <v-flex>adsadsadas</v-flex>
      </v-layout>
    </v-card>
  </v-container>
</template>
<script>
import { mapActions } from 'vuex';

export default {
  components: {},
  data() {
    return {
      selected: [],
      selectedB: [],
      itemsListB: [],
      itemsListA: [],
      dialog: false
    };
  },
  created() {},
  methods: {
    // ...mapActions('undpriority', [
    //   'getDataInSetPrioritySaleChannel',
    //   'setDataPrioritySaleChannel'
    // ])
  }
};
</script>
<style>
.vbutton {
  background-color: rgb(0, 105, 92) !important;
  color: #ffffff !important;
}

.full_text_table {
  white-space: nowrap;
}
</style>
