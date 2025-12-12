declare namespace Api {
  namespace Activity {
    interface ActivityManageDTO {
      name: string
      coverUrl?: string
      startTime: string
      endTime: string
      category?: string
      venueId: number
      organizer?: string
      contactPhone?: string
      intro?: string
      addressCache?: string
      onlineStatus?: number
    }

    interface ActivityManageVO extends ActivityManageDTO {
      id: number
      venueName?: string
      viewCount?: number
      favoriteCount?: number
      createTime?: string
      updateTime?: string
    }

    type ActivitySearchParams = Partial<
      { name: string; category: string; onlineStatus: number; startTimeFrom: string; startTimeTo: string } & Api.Common.CommonSearchParams
    >

    interface ActivityCommentManageDTO {
      userId: number
      content: string
      parentId?: number | null
    }

    interface ActivityCommentVO {
      id: number
      activityId: number
      userId: number
      userNickname: string
      userAvatar: string
      content: string
      parentId?: number | null
      childCount: number
      likeCount: number
      createTime: string
    }

    interface ActivityCommentDetailVO {
      comment: ActivityCommentVO
      replies: ActivityCommentVO[]
    }

    interface ActivityCommentSearchParams extends Api.Common.CommonSearchParams {
      parentId?: number
    }
  }
}
