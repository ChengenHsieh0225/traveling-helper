package com.travelinghelper.planning.infrastructure.message;

public final class MessagingConstants {
    public static final String EXCHANGE = "planning.exchange";

    public static final class RoutingKeys {
        public static final String PLAN_PUBLISHED = "plan.published";
        public static final String PLAN_UPDATED = "plan.updated";
    }

    public static final class TypeIds {
        public static final String PLAN_PUBLISHED = "planning.plan.published";
    }
}