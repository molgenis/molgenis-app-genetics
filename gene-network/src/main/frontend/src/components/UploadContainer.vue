<template>
  <div class="form">

    <div class="row form-group">
      <div class="col">
        <label for="vcf-file-select">Variant file</label>
        <b-form-file id="vcf-file-select" choose-label="Select a VCF file" v-model="file"></b-form-file><br>
        <small class="text-muted">Select a .vcf or .vcf.gz file from your hard drive</small>
      </div>
    </div>

    <div class="row form-group">
      <div class="col-md-6">
        <label for="vcf-file-select">Variant file identifier</label>
        <b-form-input :formatter="format" v-model="entityTypeId"></b-form-input>
        <small class="text-muted">Should not start with a number</small>
      </div>
    </div>

    <div class="row">
      <div class="col">
          <b-button class="btn btn-success" :disabled="!entityTypeId || file === null" @click="upload">Upload</b-button>
      </div>
    </div>

  </div>
</template>

<style>
  .custom-file-input {
    min-width: 40rem !important;
    display: block;
  }
</style>

<script>
  import { IMPORT_FILE } from '../store/actions'

  export default {
    name: 'upload-container',
    data () {
      return {
        entityTypeId: null,
        file: null
      }
    },
    methods: {
      /**
       * Remove illegal chars
       * Don't allow entity names starting with a number
       */
      format: function (value) {
        value = value.replace(/[-.*$&%^()#!@?]/g, '_')
        value = value.replace(/^[0-9]/g, '_')

        return value
      },
      /**
       * Handles @click event from the upload button
       * Dispatches: IMPORT_FILE
       */
      upload: function () {
        const formData = new FormData()
        formData.append('file', this.file)
        formData.append('entityTypeId', this.entityTypeId)
        formData.append('packageId', this.$store.state.diagnosticsPackageId)
        formData.append('action', 'ADD')
        formData.append('notify', false)

        this.$store.dispatch(IMPORT_FILE, formData)
      }
    }
  }
</script>
