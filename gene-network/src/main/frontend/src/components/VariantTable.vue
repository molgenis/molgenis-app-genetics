<template>
  <div>
    <b-table hover :items="variants" :fields="fields" :filter="filter"></b-table>
    <div class="table-download-button">
      <b-btn class="table-download-button" variant="info" @click="download">Download</b-btn>
      <br><br>
    </div>
    <a id="download-anchor" style="display:none"></a>
  </div>
</template>
<style>
  .table-download-button {
    float: right
  }
</style>
<script>
  import csv from 'to-csv'
  import { TextEncoder } from 'text-encoding'
  import { fromByteArray } from 'base64-js'

  export default {
    name: 'variant-table',
    props: ['variants'],
    methods: {
      base64EncodingUTF8: function (str) {
        const encoded = new TextEncoder('utf-8').encode(str)
        return fromByteArray(encoded)
      },
      variantsToCsv: function () {
        const data = []
        this.variants.map(function (variant) {
          data.push({
            '#CHROM': variant['#CHROM'],
            'Gene_Name': variant['Gene_Name'],
            'cDNA': variant['cDNA'],
            'pChange': variant['pChange'],
            'gavinReason': variant['gavinReason']
          })
        })
        return csv(data).replace(/\r\n/g, '\n')
      },
      download: function () {
        const result = this.variantsToCsv()

        // HTML workaround because all available file system packages on npm are broken
        const dlAnchorElem = document.getElementById('download-anchor')
        dlAnchorElem.setAttribute('href', 'data:text/csv;charset=utf-8;base64,' + this.base64EncodingUTF8(result))
        dlAnchorElem.setAttribute('download', 'patient_' + this.$route.params.entityTypeId + '.csv')
        dlAnchorElem.click()
      }
    },
    data: function () {
      return {
        fields: {
          '#CHROM': {
            label: 'Chromosome'
          },
          'Gene_Name': {
            label: 'Gene'
          },
          'cDNA': {
            label: 'cDNA'
          },
          'pChange': {
            label: 'Protein change'
          },
          'gavinReason': {
            label: 'Gavin Reason'
          }
        },
        filter: null
      }
    }
  }
</script>
