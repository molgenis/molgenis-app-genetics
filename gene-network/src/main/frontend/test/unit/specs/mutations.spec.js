import mutations from 'store/mutations'

describe('mutations', () => {
  describe('Testing mutation CREATE_ALERT', () => {
    it('Updates alert message', () => {
      const state = {
        showAlert: false,
        alert: {
          message: null,
          type: null
        }
      }
      const alert = {
        message: 'Hello',
        type: null
      }
      mutations.__CREATE_ALERT__(state, alert)
      expect(state.alert.message).to.equal('Hello')
    })

    it('Sets showAlert to true', () => {
      const state = {
        showAlert: false,
        alert: {
          message: null,
          type: null
        }
      }
      mutations.__CREATE_ALERT__(state, {
        message: 'Hello',
        type: null
      })
      expect(state.showAlert).to.equal(true)
    })
  })

  describe('Testing mutation REMOVE_ALERT', () => {
    it('Sets showAlert to false', () => {
      const state = {
        showAlert: true,
        alert: {
          message: 'Hello',
          type: null
        }
      }
      mutations.__REMOVE_ALERT__(state)
      expect(state.showAlert).to.equal(false)
    })

    it('Sets alert message to null', () => {
      const state = {
        showAlert: true,
        alert: {
          message: 'Hello',
          type: null
        }
      }
      mutations.__REMOVE_ALERT__(state)
      expect(state.alert.message).to.equal(null)
    })

    it('Sets alert type to null', () => {
      const state = {
        showAlert: true,
        alert: {
          message: 'Hello',
          type: null
        }
      }
      mutations.__REMOVE_ALERT__(state)
      expect(state.alert.type).to.equal(null)
    })
  })

  describe('Testing mutation UPDATE_JOB', () => {
    it('Updates the job', () => {
      const state = {
        job: null
      }
      const job = {
        status: 'FINISHED',
        message: 'Imported 3 demopatient entities.<br />',
        importedEntities: 'demopatient',
        username: 'admin'
      }
      mutations.__UPDATE_JOB__(state, job)
      expect(state.job).to.equal(job)
    })
  })

  describe('Testing mutation UPDATE_JOB_HREF', () => {
    it('Updates the jobHref', () => {
      const state = {
        jobHref: null
      }
      mutations.__UPDATE_JOB_HREF__(state, '/api/v2/sys_ImportRun/aaaacw7w2cd523srqr5qxfqaae')
      expect(state.jobHref).to.equal('/api/v2/sys_ImportRun/aaaacw7w2cd523srqr5qxfqaae')
    })
  })

  describe('Testing mutation SET_TOKEN', () => {
    it('Sets the token', () => {
      const state = {
        token: ''
      }
      mutations.__SET_TOKEN__(state, 'token')
      expect(state.token).to.equal('token')
    })
  })

  describe('Testing mutation SET_PATIENT_TABLES', () => {
    it('Sets the patients', () => {
      const state = {
        patients: []
      }
      const patients = [
        {id: 'demopatient', label: 'demopatient', _href: '/api/v2/sys_md_EntityType/demopatient'},
        {id: 'demopatient1', label: 'demopatient1', _href: '/api/v2/sys_md_EntityType/demopatient1'}
      ]
      mutations.__SET_PATIENT_TABLES__(state, patients)
      expect(state.patients).to.equal(patients)
    })
  })

  describe('Testing mutation SET_PHENOTYPE_FILTERS', () => {
    it('Sets the phenotype filters', () => {
      const state = {
        phenotypeFilters: []
      }
      const phenotypeFilters = [
        {id: 'HP_0100280', isActive: true, label: 'Crohn\'s disease'},
        {id: 'HP_0001249', isActive: true, label: 'Intellectual disability'}
      ]
      mutations.__SET_PHENOTYPE_FILTERS__(state, phenotypeFilters)
      expect(state.phenotypeFilters).to.deep.equal(phenotypeFilters)
    })
  })

  describe('Testing mutation TOGGLE_ACTIVE_PHENOTYPE_FILTERS', () => {
    it('Toggles phenotype filter to be not active', () => {
      const state = {
        phenotypeFilters: [
          {id: 'HP_0100280', isActive: true, label: 'Crohn\'s disease'},
          {id: 'HP_0001249', isActive: true, label: 'Intellectual disability'}
        ]
      }
      mutations.__TOGGLE_ACTIVE_PHENOTYPE_FILTERS__(state, 'HP_0100280')
      expect(state.phenotypeFilters[0].isActive).to.equal(false)
    })

    it('Toggles phenotype filter to be active', () => {
      const state = {
        phenotypeFilters: [
          {id: 'HP_0100280', isActive: true, label: 'Crohn\'s disease'},
          {id: 'HP_0001249', isActive: false, label: 'Intellectual disability'}
        ]
      }
      mutations.__TOGGLE_ACTIVE_PHENOTYPE_FILTERS__(state, 'HP_0001249')
      expect(state.phenotypeFilters[0].isActive).to.equal(true)
    })
  })

  describe('Testing mutation SET_VARIANTS', () => {
    it('Sets variants', () => {
      const state = {
        variants: []
      }
      const variants = [
        {
          '#CHROM': '6',
          ALT: 'C',
          CADD: '-0.272604',
          CAD_SCALED: '0.765',
          EFFECT: 'C|LOC101928519|intron_variant|MODIFIER|LOC101928519|transcript|NR_110860.1|pseudogene|5/10|n.721+1698A>G|||||||Benign|genomewide|Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
          Gene_Name: 'BRCA1',
          ID: '',
          INTERNAL_ID: 'Hbc9y7QnOOfHElunTxal8A',
          POS: 19144144,
          REF: 'T'
        }
      ]
      mutations.__SET_VARIANTS__(state, variants)
      const updatedVariants = [
        {
          '#CHROM': '6',
          ALT: 'C',
          CADD: '-0.272604',
          CAD_SCALED: '0.765',
          EFFECT: 'C|LOC101928519|intron_variant|MODIFIER|LOC101928519|transcript|NR_110860.1|pseudogene|5/10|n.721+1698A>G|||||||Benign|genomewide|Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
          Gene_Name: 'BRCA1',
          ID: '',
          INTERNAL_ID: 'Hbc9y7QnOOfHElunTxal8A',
          POS: 19144144,
          REF: 'T',
          cDNA: 'n.721+1698A>G',
          gavinReason: 'Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
          pChange: ''
        }
      ]
      expect(state.variants).to.deep.equal(updatedVariants)
    })
  })

  describe('Testing mutation UPDATE_VARIANT_SCORES', () => {
    it('Calculates the sum of the score of variant scores (Both phenotypes active)', () => {
      const state = {
        phenotypeFilters: [
          {id: 'HP_0100280', isActive: true, label: 'Crohn\'s disease'},
          {id: 'HP_0001249', isActive: true, label: 'Intellectual disability'}
        ],
        variants: [
          {
            '#CHROM': '6',
            ALT: 'C',
            CADD: '-0.272604',
            CAD_SCALED: '0.765',
            EFFECT: 'C|LOC101928519|intron_variant|MODIFIER|LOC101928519|transcript|NR_110860.1|pseudogene|5/10|n.721+1698A>G|||||||Benign|genomewide|Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
            Gene_Name: 'BRCA1',
            ID: '',
            INTERNAL_ID: 'Hbc9y7QnOOfHElunTxal8A',
            POS: 19144144,
            REF: 'T',
            cDNA: 'n.721+1698A>G',
            gavinReason: 'Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
            pChange: ''
          }
        ],
        geneNetworkScores: {
          HP_0001249: {
            BRCA1: 1.234
          },
          HP_0100280: {
            BRCA1: 1.123
          }
        }
      }
      mutations.__UPDATE_VARIANT_SCORES__(state)
      expect(state.variants[0].totalScore).to.equal(2.357)
    })
    it('Calculates the sum of the score of variant scores (One phenotype active)', () => {
      const state = {
        phenotypeFilters: [
          {id: 'HP_0100280', isActive: false, label: 'Crohn\'s disease'},
          {id: 'HP_0001249', isActive: true, label: 'Intellectual disability'}
        ],
        variants: [
          {
            '#CHROM': '6',
            ALT: 'C',
            CADD: '-0.272604',
            CAD_SCALED: '0.765',
            EFFECT: 'C|LOC101928519|intron_variant|MODIFIER|LOC101928519|transcript|NR_110860.1|pseudogene|5/10|n.721+1698A>G|||||||Benign|genomewide|Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
            Gene_Name: 'BRCA1',
            ID: '',
            INTERNAL_ID: 'Hbc9y7QnOOfHElunTxal8A',
            POS: 19144144,
            REF: 'T',
            cDNA: 'n.721+1698A>G',
            gavinReason: 'Variant is of "modifier" impact, and therefore unlikely to be pathogenic.',
            pChange: ''
          }

        ],
        geneNetworkScores: {
          HP_0001249: {
            BRCA1: 1.234
          },
          HP_0100280: {
            BRCA1: 1.123
          }
        }
      }
      mutations.__UPDATE_VARIANT_SCORES__(state)
      expect(state.variants[0].totalScore).to.equal(1.234)
    })
  })

  describe('Testing mutation SET_GENE_NETWORK_SCORES', () => {
    it('Sets the gene network scores', () => {
      const state = {
        geneNetworkScores: {}
      }
      const scores = [
        {
          column: 'HP_0100280',
          row: 'BRCA1',
          value: 1.123
        },
        {
          column: 'HP_0100280',
          row: 'BRCA2',
          value: 2.123
        },
        {
          column: 'HP_0100280',
          row: 'BRCA3',
          value: 3.123
        }
      ]
      mutations.__SET_GENE_NETWORK_SCORES__(state, scores)
      const geneNetworkScores = {
        HP_0100280: {
          BRCA1: 1.123,
          BRCA2: 2.123,
          BRCA3: 3.123
        }
      }
      expect(state.geneNetworkScores).to.deep.equal(geneNetworkScores)
    })
  })

  describe('Testing mutation REMOVE_GENE_NETWORK_SCORES', () => {
    it('Removes a deselected gene network score', () => {
      const state = {
        geneNetworkScores: {
          HP_0001249: {
            BRCA1: 1.234,
            BRCA2: 2.234,
            BRCA3: 3.234
          },
          HP_0100280: {
            BRCA1: 1.123,
            BRCA2: 2.123,
            BRCA3: 3.123
          }
        }
      }
      const removedFilter = {
        id: 'HP_0001249',
        isActive: true,
        label: 'Intellectual disability'
      }
      mutations.__REMOVE_GENE_NETWORK_SCORES__(state, removedFilter)
      const geneNetworkScores = {
        HP_0100280: {
          BRCA1: 1.123,
          BRCA2: 2.123,
          BRCA3: 3.123
        }
      }
      expect(state.geneNetworkScores).to.deep.equal(geneNetworkScores)
    })
    it('Removes last deselected gene network score', () => {
      const state = {
        geneNetworkScores: {
          HP_0100280: {
            BRCA1: 1.123,
            BRCA2: 2.123,
            BRCA3: 3.123
          }
        }
      }
      const removedFilter = {
        id: 'HP_0100280',
        isActive: true,
        label: 'Crohn\'s disease'
      }
      mutations.__REMOVE_GENE_NETWORK_SCORES__(state, removedFilter)
      const geneNetworkScores = {}
      expect(state.geneNetworkScores).to.deep.equal(geneNetworkScores)
    })
  })

  describe('The CLEAR_GENE_NETWORK_SCORES mutation', () => {
    it('should remove all scores', () => {
      const state = {
        geneNetworkScores: {
          HP_0001249: {
            BRCA1: 1.234
          },
          HP_0100280: {
            BRCA1: 1.123
          }
        }
      }
      mutations.__CLEAR_GENE_NETWORK_SCORES__(state)
      const emptyScore = {}
      expect(state.geneNetworkScores).to.deep.equal(emptyScore)
    })
  })
})
