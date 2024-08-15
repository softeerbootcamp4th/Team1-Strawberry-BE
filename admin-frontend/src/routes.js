import React from 'react'

const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))

// Admin menu
const Event = React.lazy(() => import('./views/event/Event'))


const routes = [
  { path: '/', exact: true, name: 'Home' },

  // Admin menu
  { path: '/event', name: 'Event', element: Event },

  { path: '/dashboard', name: 'Dashboard', element: Dashboard },

]

export default routes
