<template>
  <div v-if="hidden" style="width:100%;height:100%">
    <slot></slot>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import _ from 'lodash';
export default {
  props: {
    idRole: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      hidden: false
    };
  },

  async created() {
    var feature = await this.isHidden(this.idRole);
    this.hidden = _.isNil(feature) ? true : !feature;
  },
  methods: {
    ...mapActions('login', ['isHidden'])
  }
};
</script>
