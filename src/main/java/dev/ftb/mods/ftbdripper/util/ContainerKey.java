package dev.ftb.mods.ftbdripper.util;

import net.minecraft.world.Container;

public final class ContainerKey {
	public final Container container;

	public ContainerKey(Container c) {
		container = c;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContainerKey that = (ContainerKey) o;
		return container == that.container;
	}

	@Override
	public int hashCode() {
		return container.hashCode();
	}
}
