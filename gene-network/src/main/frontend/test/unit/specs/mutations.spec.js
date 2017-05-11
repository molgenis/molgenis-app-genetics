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
        status: "FINISHED",
        message: "Imported 3 demopatient entities.<br />",
        importedEntities: "demopatient",
        username:"admin"
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
        {id: "HP_0100280", isActive: true, label: "Crohn's disease"},
        {id: "HP_0001249", isActive: true, label: "Intellectual disability"}
      ]
      mutations.__SET_PHENOTYPE_FILTERS__(state, phenotypeFilters)
      expect(state.phenotypeFilters).to.deep.equal(phenotypeFilters)
    })
  })
})
