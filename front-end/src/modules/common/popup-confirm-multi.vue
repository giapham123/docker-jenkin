<template>
  <v-container
    class="home-app-wrapper"
    grid-list-md
    fluid
    text-xs-center
    style="height:auto"
  >
    <v-dialog v-model="showDialog" max-width="450" persistent lazy>
      <v-card>
        <v-card-title class="headline orange primary-title">
          Warning
        </v-card-title>
        <v-card-text>
          <h5>
            <!-- Would you like to save your settings? -->
          </h5>
          <h5>{{ showData }}</h5>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="teal vbutton" text @click="confirmSave">
            OK
          </v-btn>
          <v-btn color="teal vbutton" text @click="cancel">
            Cancel
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
export default {
  props: {
    showDialog: Boolean,
    dataPopup: String,
    taskName: String,
    showData: String
  },
  data() {
    return {
      isSave: false
    };
  },

  methods: {
    confirmSave() {
      this.isSave = true;
      this.$emit('save', {
        save: this.isSave,
        data: this.dataPopup,
        name: this.taskName
      });
    },
    cancel() {
      this.isSave = false;
      this.$emit('save', this.isSave);
    }
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
