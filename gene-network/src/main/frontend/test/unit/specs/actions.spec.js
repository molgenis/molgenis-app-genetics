import { expect } from 'chai'
import td from 'testdouble'

import actions from 'src/store/actions'
import { CREATE_ALERT, SET_TOKEN } from 'src/store/mutations'
import * as api from 'src/molgenisApi'

describe('Testing actions', () => {
  afterEach(() => td.reset())

  describe('Testing the LOGIN action', () => {
    it('LOGIN Success', done => {
      const loginResponse = {
        token: 'TOKEN'
      }

      const login = td.function('api.login')
      td.when(login('admin', 'admin')).thenResolve(loginResponse)
      td.replace(api, 'login', login)

      testAction(actions.__LOGIN__, null, {session: {username: 'admin', password: 'admin'}}, [
        {type: SET_TOKEN, payload: 'TOKEN'}
      ], done)
    })

    it('LOGIN Failure', done => {
      const loginResponse = {
        errors: [
          {message: 'Bad credentials'}
        ]
      }

      const login = td.function('api.login')
      td.when(login('admin', 'wrong_password')).thenReject(loginResponse)
      td.replace(api, 'login', login)

      testAction(actions.__LOGIN__, null, {session: {username: 'admin', password: 'wrong_password'}}, [
        {type: CREATE_ALERT, payload: {'message': 'Bad credentials', 'type': 'warning'}}
      ], done)
    })
  })
})

const testAction = (action, payload, state, expectedMutations, done) => {
  let count = 0

  const commit = (type, payload) => {
    const mutation = expectedMutations[count]

    expect(mutation.type).to.equal(type)

    if (payload) {
      expect(mutation.payload).to.deep.equal(payload)
    }

    count++
    if (count >= expectedMutations.length) {
      done()
    }
  }

  action({commit, state}, payload)

  if (expectedMutations.length === 0) {
    expect(count).to.equal(0)
    done()
  }
}
