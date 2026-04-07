package com.travelinghelper.planning.infrastructure.message;

public final class MessagingConstants {
    public static final String EXCHANGE = "planning.exchange";

    public static final class RoutingKeys {
        public static final String PLAN_PUBLISHED = "plan.published";
        public static final String PLAN_INFO_UPDATED = "plan.info.updated";
        public static final String PLAN_DELETED = "plan.deleted";
        public static final String PLAN_ITEM_ADDED = "plan.item.added";
        public static final String PLAN_ITEM_UPDATED = "plan.item.updated";
        public static final String PLAN_ITEM_REMOVED = "plan.item.removed";
    }

    public static final class TypeIds {
        public static final String PLAN_PUBLISHED = "planning.plan.published";
    }
}