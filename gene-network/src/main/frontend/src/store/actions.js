import { get, login, submitForm } from '../MolgenisApi'
import { CREATE_ALERT, SET_VARIANTS, SET_PATIENT_TABLES, SET_TOKEN, UPDATE_JOB, UPDATE_JOB_HREF } from './mutations'

export const GET_PATIENT = '__GET_PATIENT__'
export const IMPORT_FILE = '__IMPORT_FILE__'
export const FETCH_JOB = '__FETCH_JOB__'
export const FETCH_PATIENT_TABLES = '__FETCH_PATIENT_TABLES__'
export const LOGIN = '__LOGIN__'
export const COMPUTE_SCORE = '__COMPUTE_SCORE__'

const actions = {
  /**
   * Action to log in the current user
   *
   * @param commit commits: SET_TOKEN
   * @param state current state of the application
   */
  [LOGIN] ({commit, state}) {
    const {username, password} = state.session
    login(username, password).then((response) => {
      commit(SET_TOKEN, response.token)
    }).catch((error) => {
      commit(CREATE_ALERT, {'message': error.errors[0].message, 'type': 'warning'})
    })
  },
  /**
   * Imports file
   *
   * @param commit commits: UPDATE_JOB_HREF, CREATE_ALERT
   * @param dispatch dispatches: FETCH_JOB
   * @param state current state of the application
   * @param formData form data object containing parameters required by import API
   */
  [IMPORT_FILE] ({commit, dispatch, state}, formData) {
    commit(UPDATE_JOB, null)
    submitForm('/plugin/importwizard/importFile', 'post', formData, state.token)
      .then(response => {
        commit(UPDATE_JOB_HREF, response)
        dispatch(FETCH_JOB)
      })
      .catch((error) => {
        commit(CREATE_ALERT, {
          message: 'Failed to import file, cause: ' + error,
          type: 'danger'
        })
      })
  },
  /**
   * Fetches import job status
   *
   * @param commit commits: CREATE_ALERT, UPDATE_JOB
   * @param dispatch dispatches: FETCH_PATIENT_TABLES
   * @param state current state of the application
   */
  [FETCH_JOB] ({commit, dispatch, state}) {
    const interval = setInterval(() => {
      get({apiUrl: state.jobHref}, '', state.token).then((job) => {
        if (job.status === 'FINISHED') {
          clearInterval(interval)
          commit(CREATE_ALERT, {message: 'Import succeeded ' + job.importedEntities, type: 'info'})
          commit(UPDATE_JOB, null)
          dispatch(FETCH_PATIENT_TABLES)
          // TODO: go to screen 2, but for which of them?
        } else if (job.status === 'FAILED') {
          clearInterval(interval)
          commit(CREATE_ALERT, {message: 'Import failed. cause: ' + job.message, type: 'warning'})
          commit(UPDATE_JOB, null)
        } else {
          commit(UPDATE_JOB, job)
        }
      })
    }, 1000)
  },
  /**
   * Fetches all tables present in the diagnostics table
   *
   * @param commit commits: SET_PATIENT_TABLES
   * @param state current state of the application
   */
  [FETCH_PATIENT_TABLES] ({commit, state}) {
    get(state.session.server, '/v2/sys_md_Package?q=id==' + state.diagnosticsPackageId + '&attrs=entityTypes', state.token)
      .then(response => {
        if (response.items && response.items[0]) {
          commit(SET_PATIENT_TABLES, response.items[0].entityTypes)
        }
      }).catch((error) => {
        commit(CREATE_ALERT, {'message': error.errors[0].message, 'type': 'warning'})
      })
  },
  /**
   * Get the data based on patient ID
   *
   * @param commit commits: SET_VARIANTS
   * @param state current state of the application
   * @param patientId the ID of the patient table
   */
  [GET_PATIENT] ({commit, state}, patientId) {
    get(state.session.server, `/v2/${patientId}`, state.token)
      .then(response => {
        commit(SET_VARIANTS, response.items)
      }).catch((error) => {
        commit(CREATE_ALERT, {'message': error.errors[0].message, 'type': 'warning'})
      })
  },
  /**
   * Retrieves the GeneNetworkScore from a matrix stored on the server. Retrieval
   * is based on a phenotype and on all the genes present in the patient table
   *
   * @param commit commits:
   * @param state current state of the application
   * @param phenotype
   */
  [COMPUTE_SCORE] ({commit, state}, allPhenotypeFilters) {
    const matrixEntityId = state.matrixEntityId
    const rows = state.variants.map(function (variant) {
      return variant.Gene_Name
    }).toString()

    const phenotypeFilter = allPhenotypeFilters[0]
    get(state.session.server, '/matrix/' + matrixEntityId + '/valueByNames?rows=' +
      rows + '&columns=' + phenotypeFilter.ontologyTermName, state.token)
      .then(response => {
      // commit(SET_PATIENT, response.items)
      }).catch((error) => {
        if (error.errors === undefined) {
          commit(CREATE_ALERT, {'message': 'Error, something went really wrong: ' + error, 'type': 'danger'})
        } else {
          commit(CREATE_ALERT, {'message': error.errors[0].message, 'type': 'warning'})
        }
      })
    // const ontologyTermIRI = phenotype.ontologyTermIRI
    // const primaryID = ontologyTermIRI.substring(ontologyTermIRI.lastIndexOf('/') + 1)
  }
}

export default actions
