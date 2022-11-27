<template>
  <div class="filter-checkbox">
    <v-checkbox
      v-if="status == 'checked'"
      v-model="display_status.checked"
      @change="changeState"
    />
    <v-checkbox
      v-else-if="status == 'unchecked'"
      v-model="display_status.unchecked"
      @change="changeState"
    />
    <v-checkbox
      v-else
      value
      class="i_disable"
      indeterminate
      @change="changeState"
    />
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: ''
    }
  },

  data() {
    return {
      status: '',

      display_status: {
        checked: true,
        unchecked: false
      }
    };
  },

  watch: {
    value() {
      this.status = this.value;
    }
  },

  methods: {
    changeState() {
      switch (this.status) {
        case '':
          this.status = 'checked';
          break;
        case 'checked':
          this.status = 'unchecked';
          break;
        case 'unchecked':
          this.status = '';
          break;
      }

      this.$emit('input', this.status);
      this.$emit('change');

      this.display_status = {
        checked: true,
        unchecked: false
      };
    }
  }
};
</script>

<style>
.filter-checkbox .v-input {
  justify-content: center;
}

.filter-checkbox .i_disable i {
  color: #999 !important;
}
</style>
