package com.travelinghelper.planning.infrastructure.message;

public final class MessagingConstants {
    public static final String EXCHANGE = "planning.exchange";

    public static final class RoutingKeys {
        public static final String PLAN_PUBLISHED = "plan.published";
        public static final String PLAN_HEADER_UPDATED = "plan.header.updated";
        public static final String PLAN_DELETED = "plan.deleted";
        public static final String PLAN_ITINERARY_CHANGED = "plan.itinerary.changed";
    }

    public static final class TypeIds {
        public static final String PLAN_PUBLISHED = "planning.plan.published";
        public static final String PLAN_HEADER_UPDATED = "planning.plan.header.updated";
        public static final String PLAN_DELETED = "planning.plan.deleted";
        public static final String PLAN_ITINERARY_CHANGED = "planning.plan.itinerary.changed";
    }
}