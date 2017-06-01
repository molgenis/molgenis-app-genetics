import testAction from '../utils/action.utils'
import td from 'testdouble'

import {
  CREATE_ALERT,
  REMOVE_ALERT,
  SET_GENE_NETWORK_SCORES,
  SET_PATIENT_TABLES,
  SET_TOKEN,
  SET_VARIANTS,
  UPDATE_JOB,
  UPDATE_JOB_HREF,
  UPDATE_VARIANT_SCORES
} from 'src/store/mutations'

import actions, { FETCH_JOB, FETCH_PATIENT_TABLES } from 'src/store/actions'
import * as api from 'src/molgenisApi'

describe('actions', () => {
  describe('LOGIN', () => {
    afterEach(() => td.reset())

    it('should set token on success', done => {
      const mockedResponse = {
        token: 'TOKEN'
      }

      const login = td.function('api.login')
      td.when(login('admin', 'admin'))
        .thenResolve(mockedResponse)
      td.replace(api, 'login', login)

      const state = {session: {username: 'admin', password: 'admin'}}
      testAction(actions.__LOGIN__, null, state, [
        {type: SET_TOKEN, payload: 'TOKEN'}
      ], [], done)
    })

    it('should create alert on failure', done => {
      const mockedResponse = {
        errors: [
          {message: 'Bad credentials'}
        ]
      }

      const login = td.function('api.login')
      td.when(login('admin', 'wrong_password'))
        .thenReject(mockedResponse)
      td.replace(api, 'login', login)

      const state = {session: {username: 'admin', password: 'wrong_password'}}
      testAction(actions.__LOGIN__, null, state, [
        {type: CREATE_ALERT, payload: {'message': 'Bad credentials', 'type': 'warning'}}
      ], [], done)
    })
  })

  describe('FETCH_PATIENT_TABLES', () => {
    afterEach(() => td.reset())

    const state = {session: {server: {apiUrl: '/api'}}, diagnosticsPackageId: 'packageId', token: 'TOKEN'}

    it('should set list of patient tables on success', done => {
      const mockedResponse = {
        items: [
          {
            _href: '/api/v2/sys_md_Package/diagnostics',
            entityTypes: [
              {
                _href: '/api/v2/sys_md_EntityType/demo_patient',
                id: 'demo_patient',
                label: 'demo_patient'
              }
            ]
          }
        ]
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/v2/sys_md_Package?q=id==packageId&attrs=entityTypes', 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      const payload = [{
        _href: '/api/v2/sys_md_EntityType/demo_patient',
        id: 'demo_patient',
        label: 'demo_patient'
      }]

      testAction(actions.__FETCH_PATIENT_TABLES__, null, state, [
        {type: SET_PATIENT_TABLES, payload: payload}
      ], [], done)
    })

    it('should do nothing if response has no items', done => {
      const mockedResponse = {
        'items': [
          {
            '_href': '/api/v2/sys_md_Package/diagnostics',
            'entityTypes': []
          }
        ]
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/v2/sys_md_Package?q=id==packageId&attrs=entityTypes', 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      testAction(actions.__FETCH_PATIENT_TABLES__, null, state, [], [], done)
    })

    it('should create alert on failure', done => {
      const mockedResponse = {
        errors: [
          {message: 'No [READ] permission on entity type [Package] with id [sys_md_Package]'}
        ]
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/v2/sys_md_Package?q=id==packageId&attrs=entityTypes', 'TOKEN'))
        .thenReject(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        message: 'No [READ] permission on entity type [Package] with id [sys_md_Package]',
        type: 'warning'
      }

      testAction(actions.__FETCH_PATIENT_TABLES__, null, state, [
        {type: CREATE_ALERT, payload: payload}
      ], [], done)
    })
  })

  describe('GET_PATIENT', () => {
    afterEach(() => td.reset())

    const state = {session: {server: {apiUrl: '/api'}}, token: 'TOKEN'}
    const patientId = 'test_patient'

    it('should set variants on success', done => {
      const mockedResponse = {
        meta: 'metadata',
        items: [
          {id: 'item 1'},
          {id: 'item 2'}
        ]
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/v2/' + patientId, 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      const payload = [
        {id: 'item 1'},
        {id: 'item 2'}
      ]

      testAction(actions.__GET_PATIENT__, patientId, state, [
        {type: SET_VARIANTS, payload: payload}
      ], [], done)
    })

    it('should create alert on failure', done => {
      const mockedResponse = {
        errors: [
          {message: 'Unknown error'}
        ]
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/v2/' + patientId, 'TOKEN'))
        .thenReject(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        message: 'Unknown error',
        type: 'warning'
      }

      testAction(actions.__GET_PATIENT__, patientId, state, [
        {type: CREATE_ALERT, payload: payload}
      ], [], done)
    })
  })

  describe('COMPUTE_SCORE', () => {
    const phenotypeFilter = {id: 'HP_1234567890', label: 'Test disease'}
    const matrixEntityId = 'matrixEntityId'
    const rows = 'Gene1,Gene2,Gene3'

    const state = {
      session: {server: {apiUrl: '/api'}},
      token: 'TOKEN',
      matrixEntityId: matrixEntityId,
      variants: [
        {Gene_Name: 'Gene1'},
        {Gene_Name: 'Gene2'},
        {Gene_Name: 'Gene3'}
      ]
    }

    it('should set gene network scores, add scores to variants and remove alerts on success', done => {
      const mockedResponse = [
        {column: 'HP_1234567890', row: 'Gene1', score: 1},
        {column: 'HP_1234567890', row: 'Gene2', score: 2},
        {column: 'HP_1234567890', row: 'Gene2', score: 3}
      ]

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/matrix/' + matrixEntityId + '/valueByNames?rows=' + rows + '&columns=' + phenotypeFilter.id, 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      const payload = [
        {column: 'HP_1234567890', row: 'Gene1', score: 1},
        {column: 'HP_1234567890', row: 'Gene2', score: 2},
        {column: 'HP_1234567890', row: 'Gene2', score: 3}
      ]

      testAction(actions.__COMPUTE_SCORE__, phenotypeFilter, state, [
        {type: SET_GENE_NETWORK_SCORES, payload: payload},
        {type: UPDATE_VARIANT_SCORES},
        {type: REMOVE_ALERT}
      ], [], done)
    })

    it('should create alert when no score is found for the selected phenotype', done => {
      const mockedResponse = 'Unexpected token < in JSON string'

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/matrix/' + matrixEntityId + '/valueByNames?rows=' + rows + '&columns=' + phenotypeFilter.id, 'TOKEN'))
        .thenReject(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        message: 'No score was found for the combination of Test disease and one or more of the genes',
        type: 'danger'
      }

      testAction(actions.__COMPUTE_SCORE__, phenotypeFilter, state, [
        {type: CREATE_ALERT, payload: payload}
      ], [], done)
    })

    it('should create alert on failure', done => {
      const mockedResponse = {
        errors: [
          {message: 'Something went wrong'}
        ]
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/api'}, '/matrix/' + matrixEntityId + '/valueByNames?rows=' + rows + '&columns=' + phenotypeFilter.id, 'TOKEN'))
        .thenReject(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        message: 'Something went wrong',
        type: 'warning'
      }

      testAction(actions.__COMPUTE_SCORE__, phenotypeFilter, state, [
        {type: CREATE_ALERT, payload: payload}
      ], [], done)
    })
  })

  describe('IMPORT_FILE', () => {
    afterEach(() => td.reset())

    const state = {token: 'TOKEN'}
    const formData = {
      'file': 'file',
      'entityTypeId': 'entity_id',
      'packageId': 'package_id',
      'action': 'ADD',
      'notify': false
    }

    it('should set existing job to null, update the jobHref and start polling for job status on success', done => {
      const mockedResponse = '/api/v2/sys_ImportRun/import_job_id'

      const submitForm = td.function('api.submitForm')
      td.when(submitForm('/plugin/importwizard/importFile', 'post', formData, 'TOKEN')).thenResolve(mockedResponse)
      td.replace(api, 'submitForm', submitForm)

      const payload = '/api/v2/sys_ImportRun/import_job_id'
      testAction(actions.__IMPORT_FILE__, formData, state, [
        {type: UPDATE_JOB, payload: null},
        {type: UPDATE_JOB_HREF, payload: payload}
      ], [
        {type: FETCH_JOB}
      ], done)
    })

    it('should set existing job to null and create an alert on failure ', done => {
      const mockedResponse = 'Could not import file'

      const submitForm = td.function('api.submitForm')
      td.when(submitForm('/plugin/importwizard/importFile', 'post', formData, 'TOKEN')).thenReject(mockedResponse)
      td.replace(api, 'submitForm', submitForm)

      const payload = {
        message: 'Failed to import file, cause: Could not import file',
        type: 'danger'
      }

      testAction(actions.__IMPORT_FILE__, formData, state, [
        {type: UPDATE_JOB, payload: null},
        {type: CREATE_ALERT, payload: payload}
      ], [], done)
    })
  })

  describe('FETCH_JOB', () => {
    afterEach(() => td.reset())

    const state = {jobHref: '/v2/api/jobHref', token: 'TOKEN'}
    it('should create an alert, set existing job to null and set list of patient of patient tables when job is finished', done => {
      const mockedResponse = {
        status: 'FINISHED',
        importedEntities: 'test_entity'
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/v2/api/jobHref'}, '', 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        message: 'Import succeeded test_entity',
        type: 'info'
      }

      testAction(actions.__FETCH_JOB__, null, state, [
        {type: CREATE_ALERT, payload: payload},
        {type: UPDATE_JOB, payload: null}
      ], [
        {type: FETCH_PATIENT_TABLES}
      ], done)
    })

    it('should create an alert and set existing job to null when job has failed', done => {
      const mockedResponse = {
        status: 'FAILED',
        message: 'Test failure'
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/v2/api/jobHref'}, '', 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        message: 'Import failed. cause: Test failure',
        type: 'warning'
      }

      testAction(actions.__FETCH_JOB__, null, state, [
        {type: CREATE_ALERT, payload: payload},
        {type: UPDATE_JOB, payload: null}
      ], [], done)
    })

    it('should update current job when job is running', done => {
      const mockedResponse = {
        status: 'RUNNING'
      }

      const get = td.function('api.get')
      td.when(get({apiUrl: '/v2/api/jobHref'}, '', 'TOKEN'))
        .thenResolve(mockedResponse)
      td.replace(api, 'get', get)

      const payload = {
        status: 'RUNNING'
      }

      testAction(actions.__FETCH_JOB__, null, state, [
        {type: UPDATE_JOB, payload: payload}
      ], [], done)
    })
  })
})
