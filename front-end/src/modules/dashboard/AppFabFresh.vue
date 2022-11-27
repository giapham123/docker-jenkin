<template>
  <v-fab-transition>
    <div>
      <v-btn
        v-show="fab"
        fab="fab"
        small
        dark="dark"
        fixed="fixed"
        right="right"
        color="#00695c"
        class="v-button-fresh"
        @click="confirmSave"
      >
        <v-icon>refresh</v-icon>
      </v-btn>
    </div>
  </v-fab-transition>
</template>

<script>
export default {
  name: 'app-fab-fresh',

  data: () => ({
    fab: true
  }),

  methods: {
    confirmSave() {
      this.isSave = true;
      this.$emit('save', true);
    }
  }
};
</script>
