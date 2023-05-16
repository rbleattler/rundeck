import type {AppLinks, RundeckContext} from '@/library'

declare global {
    interface Window {
        _rundeck: RundeckContext
        appLinks: AppLinks
    }
}