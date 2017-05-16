import PhenotypeSelectionContainer from 'components/PhenotypeSelectionContainer.vue'

describe('PhenotypeSelectionContainer', () => {
  describe('when created', () => {
    it('should use "hpo-select" as name', () => {
      expect(PhenotypeSelectionContainer.name).to.equal('phenotype-selection-container')
    })
  })

  describe('sortVariants', () => {
    it('should sort by name if totalScore is missing', () => {
      const a = {
        Gene_Name: 'aaa'
      }
      const b = {
        Gene_Name: 'bbb'
      }
      const result = PhenotypeSelectionContainer.methods.sortVariants(a, b)
      expect(result).to.equal(1)
    })

    it('should sort by score if totalScore is defined', () => {
      const a = {
        totalScore: 4
      }
      const b = {
        totalScore: 2
      }
      const result = PhenotypeSelectionContainer.methods.sortVariants(a, b)
      expect(result).to.equal(-2)
    })
  })
})
