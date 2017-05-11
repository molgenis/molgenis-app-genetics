import App from 'App.vue'

App.$store = {
  state: {
    token: null
  },
  dispatch: sinon.spy()
}

describe('App.vue', () => {
  it('should use "molgenis-app" as name', () => {
    expect(App.name).to.equal('molgenis-app')
  })

  it('should when created fetch the patients', () => {
    App.created()
    App.$store.dispatch.should.have.been.calledWith('__FETCH_PATIENT_TABLES__')
  })
})
