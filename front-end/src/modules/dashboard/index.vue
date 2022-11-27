<template>
  <v-container>
    <h2>Hi {{ me.account_id }}, Welcome to Accouting System.</h2>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  computed: {
    ...mapGetters('login', ['me'])
  }
};
</script>
