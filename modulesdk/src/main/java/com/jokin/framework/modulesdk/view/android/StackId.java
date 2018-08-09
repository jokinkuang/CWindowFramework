package com.jokin.framework.modulesdk.view.android;

public class StackId {
        /** Invalid stack ID. */
        public static final int INVALID_STACK_ID = -1;

        /** First static stack ID. */
        public static final int FIRST_STATIC_STACK_ID = 0;

        /** Home activity stack ID. */
        public static final int HOME_STACK_ID = FIRST_STATIC_STACK_ID;

        /** ID of stack where fullscreen activities are normally launched into. */
        public static final int FULLSCREEN_WORKSPACE_STACK_ID = 1;

        /** ID of stack where freeform/resized activities are normally launched into. */
        public static final int FREEFORM_WORKSPACE_STACK_ID = FULLSCREEN_WORKSPACE_STACK_ID + 1;

        /** ID of stack that occupies a dedicated region of the screen. */
        public static final int DOCKED_STACK_ID = FREEFORM_WORKSPACE_STACK_ID + 1;

        /** ID of stack that always on top (always visible) when it exist. */
        public static final int PINNED_STACK_ID = DOCKED_STACK_ID + 1;

        /** ID of stack that contains the Recents activity. */
        public static final int RECENTS_STACK_ID = PINNED_STACK_ID + 1;

        /** ID of stack that contains activities launched by the assistant. */
        public static final int ASSISTANT_STACK_ID = RECENTS_STACK_ID + 1;

        /** Last static stack stack ID. */
        public static final int LAST_STATIC_STACK_ID = ASSISTANT_STACK_ID;

        /** Start of ID range used by stacks that are created dynamically. */
        public static final int FIRST_DYNAMIC_STACK_ID = LAST_STATIC_STACK_ID + 1;

        public static boolean isStaticStack(int stackId) {
            return stackId >= FIRST_STATIC_STACK_ID && stackId <= LAST_STATIC_STACK_ID;
        }

        public static boolean isDynamicStack(int stackId) {
            return stackId >= FIRST_DYNAMIC_STACK_ID;
        }

        /**
         * Returns true if the activities contained in the input stack display a shadow around
         * their border.
         */
        public static boolean hasWindowShadow(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID || stackId == PINNED_STACK_ID;
        }

        /**
         * Returns true if the activities contained in the input stack display a decor view.
         */
        public static boolean hasWindowDecor(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID;
        }

        /**
         * Returns true if the tasks contained in the stack can be resized independently of the
         * stack.
         */
        public static boolean isTaskResizeAllowed(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID;
        }

        /** Returns true if the task bounds should persist across power cycles. */
        public static boolean persistTaskBounds(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID;
        }

        /**
         * Returns true if dynamic stacks are allowed to be visible behind the input stack.
         */
        public static boolean isDynamicStacksVisibleBehindAllowed(int stackId) {
            return stackId == PINNED_STACK_ID || stackId == ASSISTANT_STACK_ID;
        }

        /**
         * Returns true if we try to maintain focus in the current stack when the top activity
         * finishes.
         */
        public static boolean keepFocusInStackIfPossible(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID
                    || stackId == DOCKED_STACK_ID || stackId == PINNED_STACK_ID;
        }

        /**
         * Returns true if Stack size is affected by the docked stack changing size.
         */
        public static boolean isResizeableByDockedStack(int stackId) {
            return isStaticStack(stackId) && stackId != DOCKED_STACK_ID
                    && stackId != PINNED_STACK_ID && stackId != ASSISTANT_STACK_ID;
        }

        /**
         * Returns true if the size of tasks in the input stack are affected by the docked stack
         * changing size.
         */
        public static boolean isTaskResizeableByDockedStack(int stackId) {
            return isStaticStack(stackId) && stackId != FREEFORM_WORKSPACE_STACK_ID
                    && stackId != DOCKED_STACK_ID && stackId != PINNED_STACK_ID
                    && stackId != ASSISTANT_STACK_ID;
        }

        /**
         * Returns true if the input stack is affected by drag resizing.
         */
        public static boolean isStackAffectedByDragResizing(int stackId) {
            return isStaticStack(stackId) && stackId != PINNED_STACK_ID
                    && stackId != ASSISTANT_STACK_ID;
        }

        /**
         * Returns true if the windows of tasks being moved to the target stack from the source
         * stack should be replaced, meaning that window manager will keep the old window around
         * until the new is ready.
         */
        public static boolean replaceWindowsOnTaskMove(int sourceStackId, int targetStackId) {
            return sourceStackId == FREEFORM_WORKSPACE_STACK_ID
                    || targetStackId == FREEFORM_WORKSPACE_STACK_ID;
        }

        /**
         * Return whether a stackId is a stack containing floating windows. Floating windows
         * are laid out differently as they are allowed to extend past the display bounds
         * without overscan insets.
         */
        public static boolean tasksAreFloating(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID
                || stackId == PINNED_STACK_ID;
        }

        /**
         * Return whether a stackId is a stack that be a backdrop to a translucent activity.  These
         * are generally fullscreen stacks.
         */
        public static boolean isBackdropToTranslucentActivity(int stackId) {
            return stackId == FULLSCREEN_WORKSPACE_STACK_ID
                    || stackId == ASSISTANT_STACK_ID;
        }

        /**
         * Returns true if animation specs should be constructed for app transition that moves
         * the task to the specified stack.
         */
        public static boolean useAnimationSpecForAppTransition(int stackId) {
            // TODO: INVALID_STACK_ID is also animated because we don't persist stack id's across
            // reboots.
            return stackId == FREEFORM_WORKSPACE_STACK_ID
                    || stackId == FULLSCREEN_WORKSPACE_STACK_ID
                    || stackId == ASSISTANT_STACK_ID
                    || stackId == DOCKED_STACK_ID
                    || stackId == INVALID_STACK_ID;
        }

        /**
         * Returns true if the windows in the stack can receive input keys.
         */
        public static boolean canReceiveKeys(int stackId) {
            return stackId != PINNED_STACK_ID;
        }

        /**
         * Returns true if the stack can be visible above lockscreen.
         */
        public static boolean isAllowedOverLockscreen(int stackId) {
            return stackId == HOME_STACK_ID || stackId == FULLSCREEN_WORKSPACE_STACK_ID ||
                    stackId == ASSISTANT_STACK_ID;
        }

        /**
         * Returns true if activities from stasks in the given {@param stackId} are allowed to
         * enter picture-in-picture.
         */
        public static boolean isAllowedToEnterPictureInPicture(int stackId) {
            return stackId != HOME_STACK_ID && stackId != ASSISTANT_STACK_ID &&
                    stackId != RECENTS_STACK_ID;
        }

        public static boolean isAlwaysOnTop(int stackId) {
            return stackId == PINNED_STACK_ID;
        }

        /**
         * Returns true if the top task in the task is allowed to return home when finished and
         * there are other tasks in the stack.
         */
        public static boolean allowTopTaskToReturnHome(int stackId) {
            return stackId != PINNED_STACK_ID;
        }

        /**
         * Returns true if the stack should be resized to match the bounds specified by
         * {@link ActivityOptions#setLaunchBounds} when launching an activity into the stack.
         */
        public static boolean resizeStackWithLaunchBounds(int stackId) {
            return stackId == PINNED_STACK_ID;
        }

        /**
         * Returns true if any visible windows belonging to apps in this stack should be kept on
         * screen when the app is killed due to something like the low memory killer.
         */
        public static boolean keepVisibleDeadAppWindowOnScreen(int stackId) {
            return stackId != PINNED_STACK_ID;
        }

        /**
         * Returns true if the backdrop on the client side should match the frame of the window.
         * Returns false, if the backdrop should be fullscreen.
         */
        public static boolean useWindowFrameForBackdrop(int stackId) {
            return stackId == FREEFORM_WORKSPACE_STACK_ID || stackId == PINNED_STACK_ID;
        }

        /**
         * Returns true if a window from the specified stack with {@param stackId} are normally
         * fullscreen, i. e. they can become the top opaque fullscreen window, meaning that it
         * controls system bars, lockscreen occluded/dismissing state, screen rotation animation,
         * etc.
         */
        public static boolean normallyFullscreenWindows(int stackId) {
            return stackId != PINNED_STACK_ID && stackId != FREEFORM_WORKSPACE_STACK_ID
                    && stackId != DOCKED_STACK_ID;
        }

        /**
         * Returns true if the input stack id should only be present on a device that supports
         * multi-window mode.
         * @see android.app.ActivityManager#supportsMultiWindow
         */
        public static boolean isMultiWindowStack(int stackId) {
            return stackId == PINNED_STACK_ID || stackId == FREEFORM_WORKSPACE_STACK_ID
                    || stackId == DOCKED_STACK_ID;
        }

        /**
         * Returns true if the input {@param stackId} is HOME_STACK_ID or RECENTS_STACK_ID
         */
        public static boolean isHomeOrRecentsStack(int stackId) {
            return stackId == HOME_STACK_ID || stackId == RECENTS_STACK_ID;
        }

        /**
         * Returns true if this stack may be scaled without resizing, and windows within may need
         * to be configured as such.
         */
        public static boolean windowsAreScaleable(int stackId) {
            return stackId == PINNED_STACK_ID;
        }

        /**
         * Returns true if windows in this stack should be given move animations by default.
         */
        public static boolean hasMovementAnimations(int stackId) {
            return stackId != PINNED_STACK_ID;
        }

        /** Returns true if the input stack and its content can affect the device orientation. */
        public static boolean canSpecifyOrientation(int stackId) {
            return stackId == HOME_STACK_ID
                    || stackId == RECENTS_STACK_ID
                    || stackId == FULLSCREEN_WORKSPACE_STACK_ID
                    || stackId == ASSISTANT_STACK_ID
                    || isDynamicStack(stackId);
        }
    }
