import { element } from 'prop-types'
import React from 'react'

const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))

// Admin menu
const Event = React.lazy(() => import('./views/event/Event'))
const EventDetail = React.lazy(() => import('./views/event/detail/EventDetail'))
const EventParticipants = React.lazy(() => import('./views/event/detail/EventParticipants'))

const User = React.lazy(() => import('./views/user/user'))
const Purchasers = React.lazy(() => import('./views/purchaser/purchaser'))

const routes = [
  { path: '/', exact: true, name: 'Home' },

  // Admin menu
  { path: '/event', name: 'Event', element: Event },
  { path: '/event/:eventId', name: 'Event Detail', element: EventDetail},
  { path: '/event/:eventId/:subEventId', name: 'Event Participants', element: EventParticipants},

  { path: '/user', name: 'User', element: User},
  { path: '/purchaser', name: 'Purchaser', element: Purchasers},

  { path: '/dashboard', name: 'Dashboard', element: Dashboard },
]

export default routes
