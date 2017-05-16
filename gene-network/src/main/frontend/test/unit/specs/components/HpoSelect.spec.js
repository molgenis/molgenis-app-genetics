import HpoSelect from 'components/HpoSelect.vue'

describe('HpoSelect', () => {
  describe('when created', () => {
    it('should use "hpo-select" as name', () => {
      expect(HpoSelect.name).to.equal('hpo-select')
    })

    it('should set the correct default data', () => {
      expect(typeof HpoSelect.data).to.equal('function')
      const defaultData = HpoSelect.data()
      expect(defaultData.phenotypes).to.eql([])
      expect(defaultData.selectedOptions).to.eql([])
    })
  })

  describe('when selectionChanged', () => {
    it('should clear the store when called with empty filter', () => {
      HpoSelect.methods.$store = {
        state: {
          phenotypeFilters: []
        },
        dispatch: sinon.spy(),
        commit: sinon.spy()
      }

      HpoSelect.methods.selectionChanged([])
      HpoSelect.methods.$store.commit.should.have.been.calledWith('__CLEAR_GENE_NETWORK_SCORES__')
      HpoSelect.methods.$store.commit.should.have.been.calledWith('__UPDATE_VARIANT_SCORES__')
    })
  })
})
