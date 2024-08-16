import { element } from 'prop-types'
import React from 'react'

const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))

// Admin menu
const Event = React.lazy(() => import('./views/event/Event'))
const EventDetail = React.lazy(() => import('./views/event/detail/EventDetail'))

const routes = [
  { path: '/', exact: true, name: 'Home' },

  // Admin menu
  { path: '/event', name: 'Event', element: Event },
  { path: '/event/:eventId', name: 'Event Detail', element: EventDetail},

  { path: '/dashboard', name: 'Dashboard', element: Dashboard },
]

export default routes
