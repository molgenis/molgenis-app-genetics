import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm'
import VariantTable from 'src/components/VariantTable'
import { expect } from 'chai'

Vue.use(BootstrapVue)

function getRenderedText (Component, propsData) {
  const Ctor = Vue.extend(Component)
  const vm = new Ctor({propsData: propsData}).$mount()
  return vm.$el['outerHTML']
}

describe('VariantTable.vue', () => {
  it('sets the correct default data', () => {
    const actualData = VariantTable.data()
    const expectedData = {
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

    expect(typeof VariantTable.data).to.equal('function')
    expect(actualData).to.deep.equal(expectedData)
  })

  it('renders component exactly like this', () => {
    const propsData = [
      {
        '#CHROM': '1',
        'Gene_Name': 'BRCA1',
        'cDNA': 'A>T',
        'pChange': 'Arg>His',
        'gavinReason': 'Pathogenic because test'
      },
      {
        '#CHROM': '2',
        'Gene_Name': 'BRCA2',
        'cDNA': 'C>G',
        'pChange': 'Lys>Ala',
        'gavinReason': 'Benign because test'
      }
    ]
    const actualData = getRenderedText(VariantTable, {variants: propsData})
    const expectedData = '<div><br> <h3>Variant Table</h3> <p>\n    Select phenotypes to retrieve gene network scores for every variant.\n    Variants are sorted based on <em>cumulative</em> gene network score.\n  </p> <div><div class="table-search-field form-group row"><label class="col-form-label col-sm-2">Filter</label><div class="col-sm-10"><input type="text" id="b_4" rows="1" placeholder="Type to Search" class="form-control"><!----><!----></div></div> <button class="table-download-button btn btn-info btn-md">Download all variants</button></div> <table class="table table-hover"><thead><tr><th class="">Chromosome</th><th class="">Gene</th><th class="">cDNA</th><th class="">Protein change</th><th class="">Gavin Reason</th></tr></thead><tbody><tr class=""><td class="">1</td><td class="">BRCA1</td><td class="">A&gt;T</td><td class="">Arg&gt;His</td><td class="">Pathogenic because test</td></tr><tr class=""><td class="">2</td><td class="">BRCA2</td><td class="">C&gt;G</td><td class="">Lys&gt;Ala</td><td class="">Benign because test</td></tr></tbody></table> <a id="download-anchor" style="display: none;"></a></div>'

    expect(actualData).to.equal(expectedData)
  })
})
