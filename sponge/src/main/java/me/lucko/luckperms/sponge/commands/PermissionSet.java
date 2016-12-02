/*
 * Copyright (c) 2016 Lucko (Luck) <luck@lucko.me>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.sponge.commands;

import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.common.LuckPermsPlugin;
import me.lucko.luckperms.common.commands.Arg;
import me.lucko.luckperms.common.commands.CommandException;
import me.lucko.luckperms.common.commands.CommandResult;
import me.lucko.luckperms.common.commands.SubCommand;
import me.lucko.luckperms.common.commands.sender.Sender;
import me.lucko.luckperms.common.commands.utils.ArgumentUtils;
import me.lucko.luckperms.common.commands.utils.Util;
import me.lucko.luckperms.common.constants.Permission;
import me.lucko.luckperms.common.utils.Predicates;

import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.List;

public class PermissionSet extends SubCommand<SubjectData> {
    public PermissionSet() {
        super("set", "Sets a permission for the Subject", Permission.SPONGE_PERMISSION_SET, Predicates.inRange(0, 1),
                Arg.list(
                        Arg.create("node", true, "the permission node to set"),
                        Arg.create("tristate", true, "the value to set the permission to"),
                        Arg.create("contexts...", false, "the contexts to set the permission in")
                )
        );
    }

    @Override
    public CommandResult execute(LuckPermsPlugin plugin, Sender sender, SubjectData subjectData, List<String> args, String label) throws CommandException {
        String node = args.get(0);
        Tristate tristate = SpongeUtils.parseTristate(1, args);
        ContextSet contextSet = ArgumentUtils.handleContexts(2, args);

        if (subjectData.setPermission(SpongeUtils.convertContexts(contextSet), node, tristate)) {
            Util.sendPluginMessage(sender, "&aSet &b" + node + "&a to &b" + tristate.toString().toLowerCase() + "&a in context " + SpongeUtils.contextToString(contextSet));
        } else {
            Util.sendPluginMessage(sender, "Unable to set permission. Does the Subject already have it set?");
        }

        return CommandResult.SUCCESS;
    }
}
